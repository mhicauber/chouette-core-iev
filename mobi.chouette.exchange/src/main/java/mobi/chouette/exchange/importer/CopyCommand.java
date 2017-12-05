package mobi.chouette.exchange.importer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.dao.VehicleJourneyDAO;
import mobi.chouette.persistence.hibernate.ContextHolder;

@Log4j
@Stateless(name = CopyCommand.COMMAND)
public class CopyCommand implements Command {

	public static final String COMMAND = "CopyCommand";

	@EJB 
	private VehicleJourneyDAO vehicleJourneyDAO;
	
	@Resource(lookup = "java:comp/DefaultManagedExecutorService")
	ManagedExecutorService executor;

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(Context context) throws Exception {
		
		int maxCopy = 1;

		boolean result = Constant.ERROR;

		try {

			Boolean optimized = (Boolean) context.get(Constant.OPTIMIZED);
			if (optimized) {
				List<Future<Void>> futures = (List<Future<Void>>) context.get(Constant.COPY_IN_PROGRESS);
				if (futures == null) {
					futures = new ArrayList<>();
					context.put(Constant.COPY_IN_PROGRESS, futures);
				}
				while (futures.size() >= maxCopy)
				{
					for (Iterator<Future<Void>> iterator = futures.iterator(); iterator.hasNext();) {
						Future<Void> future = iterator.next();
						if (future.isDone()) iterator.remove();
					}
					if (futures.size() >= maxCopy)
					{
						for (Iterator<Future<Void>> iterator = futures.iterator(); iterator.hasNext();) {
							Future<Void> future = iterator.next();
							if (future.isDone()) iterator.remove();
							else
							{
								log.info("too many copy in progress, waiting ...");
								future.get();
								break;
							}
						}						
					}
				}
				CommandCallable callable = new CommandCallable();
				callable.buffer = (String) context.remove(Constant.BUFFER);
				callable.schema = ContextHolder.getContext();
				Future<Void> future = executor.submit(callable);
				futures.add(future);
			}

			result = Constant.SUCCESS;
		} catch (Exception e) {
			log.error(e);
			throw e;
		}

		return result;
	}

	private class CommandCallable implements Callable<Void> {
		private String buffer;
		private String schema;

		@Override
		@TransactionAttribute(TransactionAttributeType.REQUIRED)
		public Void call() throws Exception {
			Monitor monitor = MonitorFactory.start(COMMAND);
			ContextHolder.setContext(schema);
			vehicleJourneyDAO.copy(buffer);
			log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
			ContextHolder.setContext(null);
			return null;
		}

	}

	public static class DefaultCommandFactory extends CommandFactory {

		@Override
		protected Command create(InitialContext context) throws IOException {

			Command result = null;
			try {
				String name = "java:app/mobi.chouette.exchange/" + COMMAND;
				result = (Command) context.lookup(name);
			} catch (NamingException e) {
				// try another way on test context
				String name = "java:module/" + COMMAND;
				try {
					result = (Command) context.lookup(name);
				} catch (NamingException e1) {
					log.error(e);
				}
			}
			return result;
		}
	}

	static {
		CommandFactory.register(CopyCommand.class.getName(), new DefaultCommandFactory());
	}
}
