# Logcat Analyzer

_Acá va un párrafo que describa lo que es el proyecto_

## Índice

- Comenzando
- Pre-requisitos
- Sobre el contenido del repositorio
- Preparación del entorno Android
- Funcionamiento de la aplicación
- Spark streaming

## Comenzando 🚀

_Estas instrucciones te permitirán entender el código del repositorio y obtener una copia del proyecto en funcionamiento en tu máquina local._

Mira **Deployment** para conocer como desplegar el proyecto.


### Pre-requisitos 📋

_Que cosas necesitas para instalar el software y como instalarlas_

Vamos a necesitar:
- Un teléfono con sistema operativo Android 6 o superior.
- Windows/macOS/Linux
- ADB (Android Debug Bridge)
- Instancia Ubuntu 12 con Spark


### Preparación del entorno Android 🔧

Antes de instalar la aplicación debemos preparar nuestro dispositivo Android para aprovechar todas sus funcionalidades. 
Para poder tener acceso a todos los logs de logcat, se debe conceder el siguiente permiso a la aplicación:

```
android.permission.READ_LOGS
```

Este permiso es un tanto especial debido a su potencial peligrosidad en manos ajenas, es por eso que el permiso _Read_Logs_ sólo lo tienen las aplicaciones que forman parte del firmware del sistema. Para conseguir que nuestra aplicación se beneficie de este permiso, tenemos dos opciones:
### 1. Rootear el dispositivo Android
Es una vía muy poco aconsejable por la dificultad y la peligrosidad que conlleva tener acceso root, por no hablar de las incompatibilidad, actualizaciones y posibles pérdidas de memoria.

### 2. Utilizar ADB
Por la sencillez a la hora de utilizar el Android Debug Bridge, vamos a utilizar este método. ADB es una herramienta de línea de comandos que nos permitirá comunicarnos con nuestro móvil a través de una consola desde el PC. El comando adb permite realizar una variedad de accioens en el dispositivo, como instalar y depurar apps, y proporciona acceso a una shell de Unix que puedes usar para ejecutar distintos comandos en un dispositivo. 

En este caso, usaremos el ADB para conceder permisos a nuestra app.

Estos son los pasos a seguir:

#### 1. Activar opciones de desarrollador
![Logcat_Analyzer](/res/img/OpcionesDesarrollador.png)

Dirígete a la sección _Acerca del teléfono_ situada al final de los ajustes de Android.



_Y repite_

```
hasta finalizar
```

_Finaliza con un ejemplo de cómo obtener datos del sistema o como usarlos para una pequeña demo_

## Ejecutando las pruebas ⚙️

_Explica como ejecutar las pruebas automatizadas para este sistema_

### Analice las pruebas end-to-end 🔩

_Explica que verifican estas pruebas y por qué_

```
Da un ejemplo
```

### Y las pruebas de estilo de codificación ⌨️

_Explica que verifican estas pruebas y por qué_

```
Da un ejemplo
```

## Despliegue 📦

_Agrega notas adicionales sobre como hacer deploy_

## Construido con 🛠️

_Menciona las herramientas que utilizaste para crear tu proyecto_

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - El framework web usado
* [Maven](https://maven.apache.org/) - Manejador de dependencias
* [ROME](https://rometools.github.io/rome/) - Usado para generar RSS

## Contribuyendo 🖇️

Por favor lee el [CONTRIBUTING.md](https://gist.github.com/villanuevand/xxxxxx) para detalles de nuestro código de conducta, y el proceso para enviarnos pull requests.

## Wiki 📖

Puedes encontrar mucho más de cómo utilizar este proyecto en nuestra [Wiki](https://github.com/tu/proyecto/wiki)

## Versionado 📌

Usamos [SemVer](http://semver.org/) para el versionado. Para todas las versiones disponibles, mira los [tags en este repositorio](https://github.com/tu/proyecto/tags).

## Autores ✒️

_Menciona a todos aquellos que ayudaron a levantar el proyecto desde sus inicios_

* **Andrés Villanueva** - *Trabajo Inicial* - [villanuevand](https://github.com/villanuevand)
* **Fulanito Detal** - *Documentación* - [fulanitodetal](#fulanito-de-tal)

También puedes mirar la lista de todos los [contribuyentes](https://github.com/your/project/contributors) quíenes han participado en este proyecto. 
