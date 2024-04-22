# GestorDeCursosPG
Aplicación práctica desarrollada en SpringBoot, Java y MySQL que trata sobre un Gestor de Cursos.
- Para la ejecución y funcionamiento del mismo se debe crear una base de datos local en MySQL de este estilo:
{
DROP DATABASE IF EXISTS gestioncursos;
CREATE DATABASE IF NOT EXISTS gestioncursos;

USE gestioncursos;
}

- Posterior a esto solo tendría que ir a la siguiente ruta: C:\SpringBoot\Proyecto02\gestionCursos\src\main\resources, y abrir el archivo [application.properties] y cambiar estas líneas de código para adaptarlo a tus configuraciones:
spring.datasource.url=jdbc:mysql://localhost:3306/gestioncursos  (En el final debe poner el nombre de la base de datos que haya creado.)
spring.datasource.username=root (Aquí debes poner el nombre de usuario que tengas configurado en MySQL (Esta es la predeterminada).)
spring.datasource.password=root (Aquí debes poner la contraseña que tengas guardada en su MySQL (Si no tiene simplemente deje unas comillas vacías " ").)

Visto esto, la aplicación debe funcionar correctamente. ¡Ahora disfrute de la Gestión de Cursos como se debe!📚
