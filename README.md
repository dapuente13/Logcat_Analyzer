------------------------------------------------------------

<p align="center">
  <img src="res/img/titulo.png">
</p>
<h1 align="center"> Logcat Analyzer </h1>

------------------------------------------------------------


## Índice
1. [**El Proyecto**](#1-el-proyecto)
    - [Descripción del problema](#descripci%C3%B3n-del-problema)
    - [Necesidad de Big Data](#necesidad-de-big-data)
    - [Solución](#soluci%C3%B3n)
2. [**Dataset**](#2-dataset)    
3. [**Sobre el contenido del repositorio**](#3-sobre-el-contenido-del-repositorio)
4. [**Antes de empezar**](#4-antes-de-empezar)
    - [Pre-requisitos](#pre-requisitos)
    - [Preparación del entorno Android](#preparaci%C3%B3n-del-entorno-android-opcional) (opcional)
    - [Preparación del entorno EC2](#preparaci%C3%B3n-del-entorno-ec2)
5. [**Funcionamiento de la aplicación**](#5-funcionamiento-de-la-aplicaci%C3%B3n)
6. [**Funcionamiento del servidor**](#6-funcionamiento-del-servidor)
7. [**Modo de uso**](#7-modo-de-uso)
8. [**Conclusión**](#8-Conclusi%C3%B3n)
- [**Herramientas-utilizadas**](#herramientas-utilizadas)
- [**Autores**](#autores)

------------------------------------------------------------

## 1. **El proyecto**

**Logcat Analyzer** es un proyecto en el cual hemos realizado un procesamiento de logs en tiempo real (streaming) lanzados desde dispositivos Android y recopilados en una máquina Ubuntu con Spark en AWS. Para ello hemos desarrollado una app que actua como cliente mandando logs a un servidor en otra máquina distinta.

### Descripción del problema
Es una labor complicada detectar malware en Android. Algunos acercamientos tratan de analizar los ejecutables estáticamente, antes de la ejecución. El problema surge en que de esta forma no se puede observar bien el comportamiento de la aplicación.
Por ello, es necesaria alguna forma de monitorizar lo que está ocurriendo en un dispositivo. La monitorización de logs permite precisamente esto, ver qué aplicación o servicio está haciendo qué en todo momento.

### Necesidad de Big Data
Monitorizar los logs que genera un dispositivo Android localmente es una tarea pesada y costosa, ya que consumiría mucha batería además de sobrecargar la CPU.
Por este motivo hacemos uso del Big Data, para poder procesar toda la información que aporta cada log sabiendo que por cada segundo pueden llegar a generarse más de 1000 logs.
Además, si se escalase para permitir múltiples conexiones, el peso de la información incrementaría considerablemente.

### Solución
Nuestra solución se basa en obtener, monitorizar, analizar, filtrar y guardar los logs en archivos de texto para luego enviar un resumen al dispositivo móvil de las aplicaciones que han tenido un comportamiento sospechoso (se devuelve las aplicaciones o servicios que han causado algún warning junto con el número de warnings que ha levantado dicha aplicación o servicio) con el objetivo de detectar acciones inusuales o maliciosas para garantizar la seguridad del usuario.
## 2. Dataset
En el cliente (dispositivo móvil) hemos desarrollado una aplicación que hemos llamado “Logcat Analyzer”, que se conecta con el servidor (instancia EC2) y le manda los logs en un período de tiempo que escoge el usuario, entre que presiona el botón start y el stop.
Los logs los accedemos mediante _logcat_, ejecutando:
```
Process process = Runtime.getRuntime().exec(new String[]{"logcat", "-d"});
```

A continuación eliminamos su contenido para que cuando volvamos a leerlos no haya contenido repetido:
```
Runtime.getRuntime().exec(new String[]{"logcat", "-c"});
```

Una vez obtenidos los logs, a los cuales les concatenamos un id antes de cada log para distinguir entre distintos dispositivos, creamos un socket para conectarnos con el server y los mandamos a la instancia a través de dicho socket.

El formato de los logs es así:

```
01-18 14:47:47.997  1390  1667 I chatty  : uid=1000(system) WifiP2pService expire 6 lines
01-18 14:47:48.000  2006  2348 I DeepNoDisturbP: handleStateChanged reason=15
01-18 15:52:49.310  1786  1786 W HwLockScreenPanel: Handle message: 7
01-18 15:52:49.472 24944 24944 W com.whatsapp: type=1400 audit(0.0:274475): avc: granted { getattr } for pid=24944 comm=4D65646961446F776E6C6F61642023 name="/" dev="sdcardfs" ino=11048
01-18 15:52:49.472 24944 24944 W com.whatsapp: type=1400 audit(0.0:274476): avc: granted { write } for pid=24944 comm=4D65646961446F776E6C6F61642023 name=".Shared" dev="sdcardfs" ino=20790
```

El primer campo representa la fecha de escritura del log, seguido por la hora. Luego viene el IP del proceso que lo generó y el del hilo. La letra siguiente es el nivel de log, que representa su prioridad (mecionada posteriormente). Le sigue una etiqueta de identificación de la aplicación o servicio que provocaron el log y, por último, el texto. Estos son los niveles de log posibles:
```
V / Verbose: la menor prioridad, con más información de la necesaria
D / Debug: mensajes útiles para la depuración de aplicaciones
I / Info: mensajes de información
W / Warning: mensajes con advertencias
E / Error: errores de la aplicación
F / Fatal: errores graves
```

## 3. Sobre el contenido del repositorio

Este repositorio está compuesto por 4 directorios principales y el archivo .apk instalable en Android:

- Android: Aquí encontrarás el proyecto de Android Studio entero con todos los ficheros que hemos obtenido al hacer build. Es necesario por si se quiere modificar o construir otra aplicación en el IDE de Android.
- src: Este directorio hace la función de acceso directo a los ficheros más importantes de _Android_. Aquí se almacenan las clases java de la app, los layout y el AndroidManifest.xml. Este último archivo recoge entre otras cosas los permisos que nuestra aplicación pide a Android.
- .apk: Esta es el aplicación que se debe instalar en el dispositivo Android.
- spark: Aquí recogemos los scripts necesarios para ejecutar en Apache Spark y conseguir enviar los logs por streaming.
- res: Este es un directorio donde almacenamos recursos necesarios para este repositorio. El usuario no debe prestar atención a este directorio.
- testCase: Contiene el output de un caso de prueba.

## 4. Antes de empezar

_Estas instrucciones te permitirán entender el código del repositorio y obtener una copia del proyecto en funcionamiento en tu máquina local._

### Pre-requisitos

_Que cosas necesitas para instalar el software y como instalarlas_

Vamos a necesitar:
- Logcat Analyzer app (en este repositorio)
- Un teléfono con sistema operativo Android 6 o superior.
- Windows/macOS/Linux
- ADB (Android Debug Bridge) (opcional)
- Instancia Ubuntu 12 con Spark


### Preparación del entorno Android (opcional)

Los siguientes pasos son opcionales, para aquell@s que quieran hacer un análisis de todos los logs que lanza nuestro dispositivo Android.

Antes de instalar la aplicación debemos preparar nuestro dispositivo Android para aprovechar todas sus funcionalidades. Este paso es opcional. 
Para poder tener acceso a todos los logs de logcat (y no solo a los de la aplicación), se debe conceder el siguiente permiso a la aplicación:

```
android.permission.READ_LOGS
```

Este permiso es un tanto especial debido a su potencial peligrosidad en manos ajenas, es por eso que el permiso _Read_Logs_ sólo lo tienen las aplicaciones que forman parte del firmware del sistema. Para conseguir que nuestra aplicación se beneficie de este permiso, tenemos dos opciones:
#### 1. Rootear el dispositivo Android
Es una vía muy poco aconsejable por la dificultad y la peligrosidad que conlleva tener acceso root, por no hablar de las incompatibilidad, actualizaciones y posibles pérdidas de memoria.

#### 2. Utilizar ADB
Por la sencillez a la hora de utilizar el Android Debug Bridge, vamos a utilizar este método. ADB es una herramienta de línea de comandos que nos permitirá comunicarnos con nuestro móvil a través de una consola desde el PC. El comando adb permite realizar una variedad de accioens en el dispositivo, como instalar y depurar apps, y proporciona acceso a una shell de Unix que puedes usar para ejecutar distintos comandos en un dispositivo. 

En este caso, usaremos el ADB para conceder permisos a nuestra app.

Estos son los pasos a seguir:

### 1. Activar opciones de desarrollador
![OpcionesDesarrollador1](/res/img/OpcionesDesarrollador1.png)

Dirígete a la sección _Acerca del teléfono_ situada al final de los ajustes de Android.
Una vez ahí, debes pulsar 7 veces sobre el apartado _Número de compilación_ (o _Versión del Kernel_ depende del dispositivo) hasta que salga un pop-up que te confirmará que las opciones de desarrollador están ya activadas.
Al volver a la ventana de ajustes, ahora veremos la sección de Opciones de Desarrollador.

### 2. Descargar ADB en el PC
![ADB_Download](/res/img/ADB_Download.PNG)
Descarga SDK para Windows/macOS/Linux: https://developer.android.com/studio/releases/platform-tools

### 3. Conceder permisos
- Conecta tu teléfono via USB al PC.
- Dentro de _Opciones de Desarrollador_, activa la _Depuración por USB_. Te saldrá un mensaje pidiendo tu confirmación, pulsa _OK_.
![USB_Debug](/res/img/USB_Debug.png)

- En el PC, extraemos el contendio del .zip que te has descargado previamente con el ADB y abre una consola dentro de la carpeta _platform-tools_.
![CMD_P](/res/img/CMD_P.PNG)

- Con el siguiente comando comprobaremos que se detecta el móvil:
```
adb devices
```

- A continuación daremos permisos a nuestra aplicación:
```
adb shell pm grant com.app.netcat android.permission.READ_LOGS
```
![CMD_A](/res/img/CMD_A.PNG)

- Ya podríamos quitar el USB del PC. 

Recordamos que estos pasos son opcionales y con ellos únicamente conseguiremos analizar todos los logs que se lanzan.

### Preparación del entorno EC2
Partiremos de la base de que el usuario ha creado una instancia EC2 con Linux y se encuentra conectado a ella.
Primero es necesario instalar Java:
```
sudo apt-add-repository ppa:webupd8team/java
sudo apt-get update
sudo apt install openjdk-8-jdk
```

Para comprobar la instalación, ejecutar:
```
java -version
```

Instalamos Scala:
```
sudo apt-get install scala
```

Para comprobar la instalación, ejecutar:
```
scala -version
```

Instalamos Python:
```
sudo apt-get install python
```

Para comprobar la instalación, ejecutar:
```
python -h
```

Instalamos Spark:
```
sudo curl -O http://d3kbcqa49mib13.cloudfront.net/spark-2.2.0-bin-hadoop2.7.tgz
sudo tar xvf ./spark-2.2.0-bin-hadoop2.7.tgz
sudo mkdir /usr/local/spark
sudo cp -r spark-2.2.0-bin-hadoop2.7/* /usr/local/spark
```

A continuación añadir _/usr/local/spark/bin_ a _PATH_ en _.profile_. Para ello, añadir la siguiente línea al final del archivo _~/.profile_:
```
export PATH="$PATH:/usr/local/spark/bin"
```

Ejecutar _source ~/.profile_ para actualizar el PATH de la sesión.
Incluirel hostname interno y la IP a _/etc/hosts_
```
cat /etc/hosts
127.0.0.1 localhost
172.30.4.210 ip-172-30-4-210
```

Lo siguiente es descargar los contenidos de la carpeta _Spark_.

<img src="/res/img/7.PNG" width="300" />

Para la conexión con el servidor, se debe añadir al security group una regla que acepte conexiones TCP entrantes y salientes con el puerto 1234.
A continuación se debe instalar _socat_ en el servidor:
```
sudo apt-get install socat
```


## 5. Funcionamiento de la aplicación.

Una vez instalemos la .apk del repositorio y abramos la aplicación, obtendremos la siguiente interfaz:

<img src="/res/img/6.png" width="300" />

#### 1. En este campo ingresaremos la dirección IP a la que queremos conectarnos.
#### 2. De igual forma, en este campo especificamos el puerto del servidor (en el servidor está establecido el puerto 1234).
#### 3. Aquí tenemos la opción de añadir palabras clave para poder filtrar los logs. Las keywords deben estar separadas por comas. Si se deja en blanco se mandarán todos los logs.
#### 4. El botón _SYSTEM APPS_ desplegará los paquetes del sistema. Aquellos paquetes seleccionados se añadiran al filtro.
#### 5. El botón _USER APPS_ desplegará los paquetes de las aplicaciones instaladas por el usuario. Aquellas aplicaciones seleccionadas se añadiran al filtro.
#### 6. El _STATUS_ mostrará el estado de la conexión con el servidor (OFFLINE / RUNNING / STOPPED).
#### 7. El botón _START_ conectará el dispositivo con el servidor seleccionado.
#### 8. El botón _STOP_ detendrá la conexión. Una vez detenida, pasará a tener el valor _Get Results_, el cual creará un diálogo con los resultados obtenidos.


- Introduciendo la keyword _cam_:
<img src="/res/img/2.png" width="300" />

- Selección de paquetes del sistema con el botón _SYSTEM APPS_:
<img src="/res/img/3.png" width="300" />

- Selección de aplicaciones instaladas con el botón _USER APPS_:
<img src="/res/img/4.png" width="300" />

<img src="/res/img/5.png" width="300" />

- Resultados obtenidos
<img src="/res/img/results.jpg" width="300" />

## 6. Funcionamiento del servidor
El script _connection.sh_ es básicamente un comando _socat_ que redirecciona el tráfico TCP obtenido por el puerto 1234 a localhost por el puerto 9999.
Esto permite que la IP del móvil no sea relevante para el servidor, cualquiera se puede conectar.

El _analyser.py_ es un archivo que ejecuta un spark streaming. Mediante un socket va recibiendo los logs, que filtrará por nivel de log y reducirá según la etiqueta.
Almacena todos los logs recibidos en _AndroidLogs/_ y el resultado de la reducción en _SuspiciousList/_.

<img src="/res/img/9.PNG" width="300" />

<img src="/res/img/10.PNG" width="300" />

Por último, el script _sendData.sh_ junta el resultado de _SuspiciousList/_ en un txt y lo envía por netcat al dispositivo móvil.

## 7. Modo de uso
En el servidor requerimos dos pestañas abiertas. En una de ellas debemos tener el script _connection.sh_ ejecutándose.

Seguidamente ejecutaríamos el spark streaming en la otra pestaña con:
```
spark-submit analyser.py localhost 9999
```

Va a estar devolviendo errores hasta que no haga conexión, por lo que configuramos la aplicación móvil y pulsamos el botón _START_.
Pasados 8 segundos (hemos puesto un sleep por si acaso), empezarán a llegar logs, que se imprimirán por pantalla.

<img src="/res/img/8.PNG" width="300" />

Cuando el usuario desee pararlo (se recomiendan pocos segundos, porque el móvil puede no soportar la carga), debe pulsar el botón _STOP_ y después interrumpir la ejecución de spark y del script _connection.sh_ con Ctrl^C.

Por último, ejecutar el script _sendData.sh_ y pulsar el botón _GET RESULTS_ en el móvil. Pasado un tiempo se recibirán en el dispositivo y se imprimirán en forma de alerta.


## 8. Conclusión
Trabajo futuro:
* Automatizar la conexión cliente-servidor.
* Permitir múltiples conexiones simultáneas al servidor.
* Implementar un método de machine learning para que aprenda de casos de logs benignos y malignos y sea capaz de discernirlos.

------------------------------------------------------------
## Herramientas utilizadas

* [Spark Streaming](https://spark.apache.org/docs/latest/streaming-programming-guide.html) - Creación del servidor y procesamiento de logs
* [Android Studio](https://developer.android.com/studio?hl=es) - Usado para crear la aplicación
* [AWS](https://aws.amazon.com/es/) - Usado para lanzar la instancia de linux con Spark

## Autores

* **Ramón Costales de Ledesma** - [Rymond3](https://github.com/Rymond3)
* **Jose Ignacio Daguerre Garrido** - [joseignaciodg](https://github.com/joseignaciodg)
* **Daniel Puente Arribas** - [dapuente13](https://github.com/dapuente13)
