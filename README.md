HOW TO RUN
==========

* `gradle run -PappArgs="['file_path']"`

* ie: `gradle run -PappArgs="['./src/test/resources/bigger_example.csv']"`

The file must have the format like the ones in src/test/resources

Resolution with TDD:
====================

Descripción en español:
Un agente de bolsa quiere evaluar las decisiones que toma para comprar y vender acciones. 

Como el agente tiene varias estrategias diferentes para comprar o vender, necesita un programa que pueda evaluar estas estrategias para un mes historico en particular e indicarle, cual de todas las estrategias posibles es la mas conveniente. 

Para cada mes el agente tiene una cantidad de dinero en efectivo y un listado de acciones para varias empresas con la cotización diaria de cada acción (ver ejemplo). Por cada una de estas cotizaciones diarias el agente decide si comprar o vender la acción en base a una de las estrategias disponibles. 

Las estrategias que quiere evaluar son: 

Estrategia 1)
a) comprar una acción si el precio cayo al menos un 1% con repecto a la cotización del día anterior 
b) vender una acción si el precio subió 2% o más con respecto al día anterior 
Estrategia 2) 
a) comprar una acción, si sucede 1a) o si el precio equivale al menos al doble del promedio de las cotizaciones de la acción hasta esa fecha 
b) vender una acción luego de 5 días de haberla comprado. 

Al final la ejecución del programa se debe informar cual fue la estrategia que ganó más dinero. 
Además es necesario llevar un registro de todas las compras/ventas que se hayan realizado en cada estrategia para un posterior análisis más detallado. 

Tener en cuenta que 
-Cada ves que se decide realizar una compra, esta consiste en invertir 1000 pesos comprando todas las acciones que se puedan comprar con ese dinero según la cotización de la fecha 
-Cada vez que se decide vender una accion, se venden todo lo que se haya comprado de esa accion 
-El último día del mes no pueden quedar acciones sin vender, si quedaron acciónes porque no se activó ninguna decisión de venta, se deben vender todas al precio de ese día y quedarse sólo con dinero en efectivo 
-El programa inicia con 1 millón de pesos en efectivo 
-No es necesario implementar persistencia ni UI 
-Realizar el ejercicio mediante TDD 

Ejemplo del listado de acciones con su cotización: 
`Accion, Fecha, Precio
YPF; 1/4/2014; $290
TS; 1/4/2014; $215,5
GGAL; 1/4/2014; $13,45
YPF; 2/4/2014; $294
TS; 2/4/2014; $216,5
GGAL; 2/4/2014; $13,25
YPF; 3/4/2014; $288
TS; 3/4/2014; $216
GGAL; 3/4/2014; $12,80`
etc...