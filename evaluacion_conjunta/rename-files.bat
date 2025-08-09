@echo off
echo Renombrando archivos para el sistema agricola...

cd api-publicaciones\src\main\java\publicaciones\model
ren Autor.java Agricultor.java
ren Libro.java Cosecha.java
ren Paper.java Factura.java

cd ..\..\..\..\..\..\dto
ren AutorDto.java AgricultorDto.java
ren LibroDto.java CosechaDto.java

cd ..\..\..\..\..\..\repository
ren AutorRepository.java AgricultorRepository.java
ren LibroRepository.java CosechaRepository.java
ren PaperRepository.java FacturaRepository.java

cd ..\..\..\..\..\..\service
ren AutorService.java AgricultorService.java
ren LibroService.java CosechaService.java

cd ..\..\..\..\..\..\controller
ren AutorController.java AgricultorController.java
ren LibroController.java CosechaController.java

echo Archivos renombrados exitosamente!
pause
