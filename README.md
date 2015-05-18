# Chouette [![Build Status](https://travis-ci.org/afimb/chouette.png)](http://travis-ci.org/afimb/chouette?branch=master)

Chouette is a java open source project on transport offer. It's divided in differents module : 
* chouette-iev : REST server (ear)
* mobi.chouette.api : Rest api implementation
* mobi.chouette.command : Command mode standalone program (Import, Export and Validation actions)
* mobi.chouette.common : common classes and interfaces
* mobi.chouette.dao : Dao inmplementation for model persistence (EJB)
* mobi.chouette.exchange : Common classes, interfaces and commands for exchange purpose
* mobi.chouette.exchange.gtfs : Specific commands for GTFS exchange and validation purpose
* mobi.chouette.exchange.hub : Specific commands for HUB exchange purpose
* mobi.chouette.exchange.kml : Specific commands for KML exchange purpose
* mobi.chouette.exchange.neptune : Specific commands for Neptune exchange and validation purpose
* mobi.chouette.exchange.netex : Specific commands for NeTEx exchange purpose
* mobi.chouette.exchange.validator : Specific commands for common validation purpose
* mobi.chouette.model : JPA entities modelisation
* mobi.chouette.persistence.hibernate : Hibernate specific tools
* mobi.chouette.persistence.service : Job and tasks managment

For more information see [Architecture Documentation](http://www.chouette.mobi/docs/) 

Feel free to test and access to the demonstration web site at [http://www.chouette.mobi](http://appli.chouette.mobi/chouette2/users/sign_in). Two types of access are granted : 
* A demo organisation with a set of data
  * login : demo@chouette.mobi
  * password : chouette
* Create your own organisation : Must follow the link "Sign up" ("S'inscrire")

## Release Notes

The release notes can be found in [CHANGELOG](./CHANGELOG.md) file 

## Requirements
 
This code has been run and tested on [Travis](http://travis-ci.org/afimb/chouette?branch=master) with : 
* oraclejdk7
* oraclejdk8
* openjdk7


## External Deps
On Debian/Ubuntu/Kubuntu OS : 
```sh
sudo apt-get install postgresql 
sudo apt-get install pgadmin3 
sudo apt-get install openjdk-7-jdk 
sudo apt-get install git
```

## Installation
 
On debian, chouette can also be installed as package : see [debian packages](http://packages.chouette.cityway.fr/debian/chouette)

Install [Postgres](./doc/install/postgresql.md) 

Install [Maven](./doc/install/maven.md)

Install [Wildfly](./doc/install/wildfly.md) 

### from sources
Create test and development databases : 
```sh
createdb -E UTF-8 -T template1 chouette_dev
createdb -E UTF-8 -T template1 chouette_test
```

Get git repository :
```sh
cd workspace
git clone -b V3_0 git://github.com/afimb/chouette
cd chouette
```

Test :

```sh
mvn test
```

Deployment :

change data storage directory (USER_HOME by default)
copy properties file [iev.properties](./doc/iev.prooperties) in /etc/chouette/iev/ directory
change property iev.directory

deploy ear (wildfly must be running)
```sh
mvn -DskipTests install
```

## from binary
download chouette.ear from [maven repository](http://maven.chouette.mobi/chouette_iev)

in wildfly installation repository : 
```sh
bin/jboss-cli.sh connect, deploy --force  (path to ...)/chouette.ear
```

## More Information
 
More information can be found on the [project website on GitHub](.). 
An exhaustive technical documentation in French is avalailable [here](http://www.chouette.mobi/docs/)


## License
 
This project is licensed under the CeCILL-B license, a copy of which can be found in the [LICENSE](./LICENSE.md) file.

## Release Notes

The release notes can be found in [CHANGELOG](./CHANGELOG.md) file 
 
## Support
 
Users looking for support should file an issue on the GitHub [issue tracking page](../../issues), or file a [pull request](../../pulls) if you have a fix available.
