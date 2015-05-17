# portlets-app  

Portlets application and related jobs  


*all commands to be run in portlets-app directory*

## Web UI dependencies setup

	cd public ; bower install ; cd ..

## To run local dev mode

	activator ~run

## To run local Docker using sbt-native-packager  

	sudo ./activator docker:publishLocal  
	sudo docker run -p 9000:9000 portlets-svc:1.0-SNAPSHOT  

#### Create zip for BeanStalk docker container  

	./ebzip.sh  

## To deploy on Heroku  

	activator stage deployHeroku  

