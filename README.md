# portlets-svc

Portlets service and related jobs  

## To run local Docker using sbt-native-packager

	cd <project-home:portlets-svc>  
	sudo ./activator docker:publishLocal
	sudo docker run -p 9000:9000 portlets-svc:1.0-SNAPSHOT

##### Create zip for BeanStalk docker container 

	cd <project-home:portlets-svc>  
	./ebzip.sh
