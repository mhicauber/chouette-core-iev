package mobi.chouette.exchange.kml.exporter;

import java.io.IOException;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.dao.LineDAO;
import mobi.chouette.model.Line;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

@Log4j
@Stateless(name = DaoKmlLineProducerCommand.COMMAND)
public class DaoKmlLineProducerCommand implements Command, Constant {
	public static final String COMMAND = "DaoKmlLineProducerCommand";

	@Resource
	private SessionContext daoContext;

	@EJB
	private LineDAO lineDAO;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean execute(Context context) throws Exception {
		boolean result = ERROR;

		Monitor monitor = MonitorFactory.start(COMMAND);

		try {
			Long lineId = (Long) context.get(LINE_ID);
			Line line = lineDAO.find(lineId);
			InitialContext initialContext = (InitialContext) context.get(INITIAL_CONTEXT);
			
			Command export = CommandFactory.create(initialContext, KmlLineProducerCommand.class.getName());
			
			context.put(LINE, line);
			result = export.execute(context);

			//daoContext.setRollbackOnly();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
		}

		return result;
	}

	public static class DefaultCommandFactory extends CommandFactory {

		@Override
		protected Command create(InitialContext context) throws IOException {
			Command result = null;
			try {
				String name = "java:app/mobi.chouette.exchange.kml/"
						+ COMMAND;
				result = (Command) context.lookup(name);
			} catch (NamingException e) {
				log.error(e);
			}
			return result;
		}
	}

	static {
		CommandFactory.factories.put(DaoKmlLineProducerCommand.class.getName(), new DefaultCommandFactory());
	}


}
