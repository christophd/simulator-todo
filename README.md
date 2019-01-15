Todo API Simulator ![Logo][1]
================

This is a standalone simulator application for a simple todo web application.
The simulator is a normal Spring Boot web application and can be started as standalone server with embedded Tomcat application server. 
Its simulation capabilities are based on the [Citrus][2] framework and primarily implemented to fit the [Syndesis](https://github.com/syndesisio/syndesis) project requirements. 

Read the basic Citrus simulator [user manual](https://citrusframework.org/citrus-simulator/) for general information.

Clients are able to access the operations defined in the [swagger.json](src/main/resources/static/swagger.json) Open API specification.

When the simulator receives a new request on one of these endpoints the simulator will respond with a valid response in respect to the Swagger API specification. The scenarios are auto generated from the
specification and respond with a random test data based on the specification.

In addition to these generic scenarios you can add your very customized and specific scenario that provides a proper response to your request.

Quick start
---------

You can build the simulator application locally with Maven:

```
mvn clean install
```

This will compile and package all resources for you. After that you are able to run the simulator on your local system with:

```
mvn spring-boot:run
```

Open your browser and point to [http://localhost:8080](http://localhost:8080). You will see the simulator user interface with all available scenarios and 
latest activities. 

You can execute some predefined Citrus integration tests now in order to get some interaction with the simulator. Open the Maven project in your favorite IDE and
run the tests in `src/test/java`. You should see the tests calling operations on the simulator in order to receive proper responses. The simulator user interface should track those
interactions accordingly.

You can also run the tests from command line with:

```
mvn verify -Pintegration
```

Simulator UI
---------

The simulator provides a small web user interface that provides some information on the latest activities such as executed scenarios and exchanged messages. You can access the UI via browser pointing to:

```
http://localhost:8080
```

Run as Docker container
---------

You can run the simulator application as a container in Docker. First of all you need to build the Docker images with:

```
mvn docker:build
```

After that you are ready to run the image in Docker with:

```
mvn docker:start
```

This starts up a new container with the simulator application running in Docker.

Run in Openshift
---------

The simulator application is able to run as POD in a Kubernetes environment such as Openshift. Most easy way to setup a local Openshift cluster is to follow the instructions for [Minishift]() 
which is a single node cluster on your local machine.

When running in Openshift be sure to login to the project via `oc` CLI as next step. You can copy the login command from the upper right drop down menu when viewing the Openshift console in your browser.

You can open the Openshift console with Minishift in your browser using this command:

```
minishift console
```

The login command looks like this:

```
oc login https://192.168.64.10:8080 --token=[token]
```

Ip address and token may differ according to your local installation. As an alternative you can login with username password credentials:

```
oc login -u developer
```

Now everything is ready for the simulator deployment to your local Minishfit Openshift cluster. By default the simulator is built and deployed in a project named `syndesis`. You can adjust this project name in the Maven
POM configuration in the properties section:

```
<openshift.namespace>syndesis</openshift.namespace>
```

Now let's finally build the simulator image on the cluster.

```
mvn package fabric8:build -Pfabric8
``` 

This `fabric8:build` command is automatically triggering a new build on the Openshift cluster. You can review the build in the Openshift web console, too. 

After that we are ready to deploy.

```
mvn fabric8:deploy -Pfabric8
```

This step will create all resources in Openshift needed to run the simulator as POD in your local cluster. The Maven command uses a profile `fabric8` which will automatically read some cluster information
and create some build properties on the fly. These properties are:

* openshift.domain (something like `192.168.64.10.nip.io`)
* openshift.registry (something like `172.30.1.1:5000`)

You can also leave out the profile and set the properties explicitly on the deploy command:

```
mvn fabric8:deploy -Dopeshift.domain=myopenshift.domain.io -Dopenshift.registry=172.30.1.1:5000
```

This performs the complete deployment in your local Openshift cluster. After that you see new deployment configs, services, pod and route in the Openshift web console. You can access the simulator in your browser via:

```
http://simulator-todo.192.168.64.10.nip.io
```

The IP address may be different on your installation. You can see the simulator URL via Openshift console, too.

Information
---------

Read the [user manual](https://citrusframework.org/citrus-simulator/) for detailed instructions and features.
For more information on Citrus see [citrusframework.org][2], including a complete [reference manual][3].

 [1]: https://citrusframework.org/img/brand-logo.png "Citrus"
 [2]: https://citrusframework.org
 [3]: https://citrusframework.org/reference/html/
