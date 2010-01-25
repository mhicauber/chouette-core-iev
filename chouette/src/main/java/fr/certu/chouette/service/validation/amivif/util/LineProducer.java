package fr.certu.chouette.service.validation.amivif.util;

import java.util.HashSet;
import java.util.Set;

import fr.certu.chouette.service.validation.amivif.Line;
import fr.certu.chouette.service.validation.amivif.TridentObject;
import fr.certu.chouette.service.validation.amivif.commun.TypeInvalidite;
import fr.certu.chouette.service.validation.amivif.commun.ValidationException;

public class LineProducer extends TridentObjectProducer {
    
    private RegistrationProducer	registrationProducer	= new RegistrationProducer(getValidationException());
    
    public LineProducer(ValidationException validationException) {
		super(validationException);
	}

	public Line getASG(amivif.schema.Line castorLine) {
		if (castorLine == null)
			return null;
		
		// TridentObject obligatoire
		TridentObject tridentObject = super.getASG(castorLine);
		Line line = new Line();
		line.setTridentObject(tridentObject);
		
		// name optionnel
		line.setName(castorLine.getName());
		
		// number optionnel
		line.setNumber(castorLine.getNumber());
		
		// publishedName optionnel
		line.setPublishedName(castorLine.getPublishedName());
		
		// transportModeName optionnel
		if (castorLine.getTransportModeName() != null)
			switch (castorLine.getTransportModeName()) {
				case amivif.schema.types.TransportModeNameType.AIR:
					line.setTransportMode(Line.TransportMode.Air);
					break;
				case amivif.schema.types.TransportModeNameType.BICYCLE:
					line.setTransportMode(Line.TransportMode.Bicycle);
					break;
				case amivif.schema.types.TransportModeNameType.BUS:
					line.setTransportMode(Line.TransportMode.Bus);
					break;
				case amivif.schema.types.TransportModeNameType.COACH:
					line.setTransportMode(Line.TransportMode.Coach);
					break;
				case amivif.schema.types.TransportModeNameType.FERRY:
					line.setTransportMode(Line.TransportMode.Ferry);
					break;
				case amivif.schema.types.TransportModeNameType.LOCALTRAIN:
					line.setTransportMode(Line.TransportMode.LocalTrain);
					break;
				case amivif.schema.types.TransportModeNameType.LONGDISTANCETRAIN:
					line.setTransportMode(Line.TransportMode.LongDistanceTrain);
					break;
				case amivif.schema.types.TransportModeNameType.METRO:
					line.setTransportMode(Line.TransportMode.Metro);
					break;
				case amivif.schema.types.TransportModeNameType.OTHER:
					line.setTransportMode(Line.TransportMode.Other);
					break;
				case amivif.schema.types.TransportModeNameType.PRIVATEVEHICLE:
					line.setTransportMode(Line.TransportMode.PrivateVehicle);
					break;
				case amivif.schema.types.TransportModeNameType.RAPIDTRANSIT:
					line.setTransportMode(Line.TransportMode.RapidTransit);
					break;
				case amivif.schema.types.TransportModeNameType.SHUTTLE:
					line.setTransportMode(Line.TransportMode.Shuttle);
					break;
				case amivif.schema.types.TransportModeNameType.TAXI:
					line.setTransportMode(Line.TransportMode.Taxi);
					break;
				case amivif.schema.types.TransportModeNameType.TRAIN:
					line.setTransportMode(Line.TransportMode.Train);
					break;
				case amivif.schema.types.TransportModeNameType.TRAMWAY:
					line.setTransportMode(Line.TransportMode.Tramway);
					break;
				case amivif.schema.types.TransportModeNameType.TROLLEYBUS:
					line.setTransportMode(Line.TransportMode.Trolleybus);
					break;
				case amivif.schema.types.TransportModeNameType.VAL:
					line.setTransportMode(Line.TransportMode.VAL);
					break;
				case amivif.schema.types.TransportModeNameType.WALK:
					line.setTransportMode(Line.TransportMode.Walk);
					break;
				case amivif.schema.types.TransportModeNameType.WATERBORNE:
					line.setTransportMode(Line.TransportMode.Waterborne);
					break;
				default:
					getValidationException().add(TypeInvalidite.InvalidTransportModeName_Line, "Le \"TransportModeName\" de la \"Line\" est inconnue.");
			}
		
		// lineEnd 0..w
		Set<String> aSet = new HashSet<String>();
		String[] castorLineEnds = castorLine.getLineEnd();
		if (castorLineEnds != null)
			for (int i = 0; i < castorLineEnds.length; i++) {
				if (aSet.add(castorLineEnds[i])) {
					try {
						(new TridentObject()).new TridentId(castorLineEnds[i]);
					}
					catch(NullPointerException e) {
						getValidationException().add(TypeInvalidite.NullTridentObjectLineEnd_Line, "Un \"objectId\" ne peut etre null.");
					}
					catch(IndexOutOfBoundsException e) {
						getValidationException().add(TypeInvalidite.InvalidTridentObject, "L'\"objectId\" "+castorLineEnds[i]+" est invalid.");
					}
					line.addLineEndId(castorLineEnds[i]);
				}
				else
					getValidationException().add(TypeInvalidite.MultipleTridentObject, "La liste \"lineEnd\" de la \"Line\" contient plusieur fois le meme identifiant ("+castorLineEnds[i]+").");					
			}
		
		// routeId 1..w
		aSet = new HashSet<String>();
		String[] castorRouteIds = castorLine.getRouteId();
		if ((castorRouteIds == null) || (castorRouteIds.length < 1))
			getValidationException().add(TypeInvalidite.NoRoute_Line, "La \"Line\" doit avoir au moins une \"Route\".");
		else
			for (int i = 0; i < castorRouteIds.length; i++) {
				if (aSet.add(castorRouteIds[i])) {
					try {
						(new TridentObject()).new TridentId(castorRouteIds[i]);
					}
					catch(NullPointerException e) {
						getValidationException().add(TypeInvalidite.NullTridentObjectRouteId_Line, "Un \"objectId\" ne peut etre null.");
					}
					catch(IndexOutOfBoundsException e) {
						getValidationException().add(TypeInvalidite.InvalidTridentObject, "L'\"objectId\" "+castorRouteIds[i]+" est invalid.");
					}
					line.addRouteId(castorRouteIds[i]);
				}
				else
					getValidationException().add(TypeInvalidite.MultipleTridentObject, "La liste \"routeId\" de la \"Line\" contient plusieur fois le meme identifiant ("+castorRouteIds[i]+").");
			}
		
		// registration optionnel
		line.setRegistration(registrationProducer.getASG(castorLine.getRegistration()));
		if (line.getRegistration() != null)
			if (line.getRegistration().getLineIdsCount() >= 1) {
				boolean notFound = true;
				for (int i = 0; i < line.getRegistration().getLineIdsCount(); i++)
					if (line.getObjectId().toString().equals(line.getRegistration().getLineId(i))) {
						if (notFound)
							notFound = false;
						line.getRegistration().removeLineId(i);
						line.getRegistration().addLine(line);
					}
				if (notFound)
					getValidationException().add(TypeInvalidite.InvalidRegistartion_Line, "La liste des \"lineId\" de la \"registration\" du \"Line\" ne contient pas son identifiant \"objectId\" ("+line.getObjectId().toString()+").");
			}
		
		// ptNetworkShortcut optionnel
		line.setPTNetworkIdShortcut(castorLine.getPtNetworkIdShortcut());
		if (line.getPTNetworkIdShortcut() != null) {
			try {
				(new TridentObject()).new TridentId(line.getPTNetworkIdShortcut());
			}
			catch(NullPointerException e) {
				getValidationException().add(TypeInvalidite.NullTridentObjectPTNetworkIdShortcut_Line, "Un \"objectId\" ne peut etre null.");
			}
			catch(IndexOutOfBoundsException e) {
				getValidationException().add(TypeInvalidite.InvalidTridentObject, "L'\"objectId\" "+line.getPTNetworkIdShortcut()+" est invalid.");
			}
		}
		
		// comment optionnel
		line.setComment(castorLine.getComment());
		
		return line;
	}
}
