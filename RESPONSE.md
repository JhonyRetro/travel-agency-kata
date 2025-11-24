# Principios SOLID del proyecto

1. **Single responsibility principle**\
En el proyecto observamos que cada clase está designada a una sola tarea, reforzado por la arquitectura hexagonal que mantiene el proyecto. Vemos cómo en cada capa se define una clase para una tarea en específico, como lo es en la capa application.
2. **Open–closed principle**\
El proyecto es flexible y abierto a modificaciones/implementaciones futuras. Un ejemplo claro es la definición de interfaces, donde luego podemos modificar o crear nuevos tipos de repositorio, así también como la creación de nuevos commandos en el paquete command.
3. **Liskov substitution principle**\
En el proyecto podemos ver un claro uso de polimorfismo en la clase CustomersController, donde tenemos un atributo de tipo CustomerRepository, permitiendo utilizar el código con cualquier bean que implemente dicha interfaz.
4. **Interface segregation principle**\
Cada módulo del proyecto se encuentra correctamente separado, donde solo implementamos aquellas operaciones que nos hagan falta. Es el caso de CustomersInJPARepository, donde utilizamos las operaciones de la interfaz JPA para operar con la base de datos.
5. **Dependency inversion principle**\
En el proyecto se utiliza la abstracción para minimizar el acoplamiento en las clases. Lo podemos ver en la propia separación de los módulos, donde vemos que la estructura de datos de un cliente es totalmente independiente de aquellas clases que la implementen.

# Patrones de diseño observados/utilizados
- Patrón Singleton: Definido en ambas clases de repositorio, donde nos aseguramos de que sólo exista una instancia del repositorio (evitando duplicados).
- Patrón Builder: Lo podemos encontrar tanto en la clase CreateCustomerCommand como en la clase CustomerEntityAdapter, donde se utiliza para construir paso a paso un cliente o adaptarlo a una entidad.
- Patrón Adapter: Es el caso de la clase CustomerEntityAdapter, que nos permite convertir objetos Customer a una entidad, y viceversa.
- Patrón Facade: Puede encontrarse en la clase CustomerController que agrupa y simplifica un conjunto de operaciones más complejas.
- Patrón Command: Claramente observable en la capa application, donde tenemos cada petición a la base de datos en una clase concreta.
- Cadena de responsabilidad: Cada petición hecha a la base de datos pasa por una serie de handlers, y podemos verlo en CustomerController.