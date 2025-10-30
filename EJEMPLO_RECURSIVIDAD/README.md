# EXPLICACION SOBRE RECURSIVIDAD INFINITA

>La recursividad infinita ocurre cuando una función se llama a sí misma sin una condición 
> de terminación adecuada, lo que provoca que la función nunca deje de ejecutarse. 
> Esto puede llevar a un desbordamiento de pila (stack overflow) y eventualmente a que el programa falle.
> 
> ![image](/images/img.png)
> 
> Este problema es muy comun en JPA sobre recurvidad infinita en las relaciones entre entidades.
> Por ejemplo, si tenemos dos entidades `A` y `B` que se refieren mutuamente,
> al serializar una instancia de `A`, se intentará serializar su referencia a `B`,
> y luego la referencia de `B` a `A`, y así sucesivamente, creando un ciclo infinito.
> 
> Para evitar la recursividad infinita en JPA, se pueden utilizar varias estrategias:
> estrategias:
> 
> 1. **Anotaciones de Jackson**: Utilizar anotaciones como `@JsonManagedReference` y `@JsonBackReference`
>    para manejar las relaciones bidireccionales.
> 2. **DTOs (Data Transfer Objects)**: Crear clases DTO que representen solo los datos necesarios
>    para la transferencia, evitando así las referencias circulares.
> 3. **Anotación @JsonIgnore**: Utilizar `@JsonIgnore` en las propiedades que no deben ser serializadas.
> 4. **Configuración de ObjectMapper**: Configurar el `ObjectMapper` de Jackson para manejar referencias cíclicas.
> 
> Al implementar estas estrategias, se puede prevenir la recursividad infinita y asegurar
> que las entidades JPA se serialicen correctamente sin errores.    
> 
> 