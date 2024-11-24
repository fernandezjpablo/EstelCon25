function generarPlazas()
{
var alertstr="";
if ($('#capacidad').val()=="") alertstr+="Número de plazas no puede estar en blanco.\n";
if ($('#camas').val()=="") alertstr+="Capacidad de camas no puede estar en blanco.\n";
if ($('#numHabitaciones').val()=="") alertstr+="Debe indicar cuántas habitaciones quiere generar.\n";
if ($('#precioAdultos').val()=="") alertstr+="Debe indicar el precio unitario para adultos de este tipo de habitación.\n";
if ($('#precioMenores').val()=="") alertstr+="Debe indicar el precio unitario para menores de 14 años de este tipo de habitación.\n";
if (alertstr=="")
{
$.ajax({
		url:"/Ezellohar/admin/plazas/GenerarPlazas",
		type:"POST",
		data:"habitaciones="+$('#numHabitaciones').val()+"&capacidad="+$('#capacidad').val()+"&planta="+$('#numPlanta').val()+"&camas="+$('#camas').val()+"&precioAdultos="+$('#precioAdultos').val()+"&precioMenores="+$('#precioMenores').val(),
		success:function(respuesta,estados){
				alert(respuesta);
				cargarListado("");
		}
});
}
else
{
	alert(alertstr);
}
}

function fraccionarPlazas()
{
var resp=confirm("¿Quieres reconvertir estas habitaciones en plazas individuales?");
if (resp)
{
var habitaciones=JSON.parse("[]");
$('.seleccion:checkbox:checked').each(function(i,elemento) {
	habitaciones.push(elemento.value);
});

$.ajax({
		url:"/Ezellohar/admin/plazas/GenerarPlazasParciales",
		type:"POST",
		data:"estado="+0+"&plazas="+habitaciones,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Habitaciones convertidas");
				cargarListado("");
				}
			else
				{
				alert(respObj["mensaje"]);
				}
		}
});
 }

}

function recombinarPlazas()
{
var resp=confirm("¿Quieres reunificar estas plazas individuales en una habitación grupal?");
if (resp)
{
var habitaciones=JSON.parse("[]");
$('.seleccionparcial:checkbox:checked').each(function(i,elemento) {
	habitaciones.push(elemento.value);
});

$.ajax({
		url:"/Ezellohar/admin/plazas/RecombinarPlazasParciales",
		type:"POST",
		data:"plazas="+habitaciones,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Plazas recombinadas");
				cargarListado("");
				}
			else
				{
				alert(respObj["mensaje"]);
				}
		}
});
 }

}

function eliminarPlazas()
{
var resp=confirm("¿Seguro que quieres eliminar las plazas seleccionadas?\nSólo se pueden eliminar las que estén en estado Bloqueado");
if (resp)
{
resp=confirm("¿Totalmente segur@?");
if (resp)
{
var habitaciones=JSON.parse("[]");
$('.seleccion:checkbox:checked').each(function(i,elemento) {
	habitaciones.push(elemento.value);
});

$.ajax({
		url:"/Ezellohar/admin/plazas/EliminarPlazas",
		type:"POST",
		data:"plazas="+habitaciones,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Plazas eliminadas (sólo las que estuvieran bloqueadas)");
				cargarListado("");
				}
			else
				{
				alert("Error al eliminar plazas: "+respObj["resultado"]);
				}
		}
});
 }
}
}

function estadoPlazasParcialesSeleccionadas(estado)
{


var habitaciones=JSON.parse("[]");
$('.seleccionparcial:checkbox:checked').each(function(i,elemento) {
	habitaciones.push(elemento.value);
});
if (habitaciones.length>0)
{
var resp=confirm("Seguro que quieres modificar el estado de las plazas individuales seleccionadas?");
if (resp)
{
$.ajax({
		url:"/Ezellohar/admin/plazas/EstadoPlazasParciales",
		type:"POST",
		data:"plazas="+habitaciones+"&estado="+estado,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Estados actualizadas");
				cargarListado("");
				}
			else
				{
				alert("Error al actualizar los estados: "+respObj["resultado"]);
				}
		}
});

}
else
{
	alert("De acuerdo, proceso cancelado.");
}
}
}

function estadoPlazasSeleccionadas(estado)
{

var habitaciones=JSON.parse("[]");
$('.seleccion:checkbox:checked').each(function(i,elemento) {
	habitaciones.push(elemento.value);
});
if (habitaciones.length>0)
{
var resp=confirm("Seguro que quieres modificar el estado de las plazas seleccionadas?");
if (resp)
{
$.ajax({
		url:"/Ezellohar/admin/plazas/EstadoPlazas",
		type:"POST",
		data:"plazas="+habitaciones+"&estado="+estado,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Estados actualizadas");
				cargarListado("");
				}
			else
				{
				alert("Error al actualizar los estados: "+respObj["resultado"]);
				}
		}
});

}
else
{
	alert("De acuerdo, proceso cancelado.");
}
}
}
function asignarNombre(id,nombre)
{
var nombre=window.prompt("Introduzca el nombre de la habitación:",nombre);
$.ajax({
		url:"/Ezellohar/admin/plazas/CambiarNombreHabitacion",
		type:"POST",
		data:"id="+id+"&nombre="+nombre,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				cargarListado("");
				}
			else
				{
				alert("Error al cambiar el nombre: "+respObj["resultado"]);
				}
		}
});
}

function asignarPlanta(id,planta)
{
var planta=window.prompt("Introduzca la planta de la habitación:",planta);
$.ajax({
		url:"/Ezellohar/admin/plazas/CambiarPlantaHabitacion",
		type:"POST",
		data:"id="+id+"&planta="+planta,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				cargarListado("");
				}
			else
				{
				alert("Error al cambiar la planta: "+respObj["resultado"]);
				}
		}
});
}

function toggleAllParciales()
{
var resultado=$("#selectAllParcial").is(':checked');
$(".seleccionparcial").each(function(){
$(this).prop("checked",resultado);
});
$('.div-parcial').each(function(){
if (resultado)
{
	$(this).show();
}
else
{
	$(this).hide();
}
});
}

function toggleAll()
{
var resultado=$("#selectAll").is(':checked');
$(".seleccion").each(function(){
$(this).prop("checked",resultado);
});
}
function cambiarCamas(id,camas)
{
var camas=window.prompt("Introduzca las camas que tiene la habitación:",camas);
if (camas!=null)
{
$.ajax({
		url:"/Ezellohar/admin/plazas/CambiarCamasHabitacion",
		type:"POST",
		data:"id="+id+"&camas="+camas,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				cargarListado("");
				}
			else
				{
				alert("Error al cambiar el número de camas: "+respObj["resultado"]);
				}
		}
});
}
}

function cambiarPlazas(id,plazas)
{
var plazas=window.prompt("Introduzca las plazas que tiene la habitación:",plazas);
if (plazas!=null)
{
$.ajax({
		url:"/Ezellohar/admin/plazas/CambiarPlazasHabitacion",
		type:"POST",
		data:"id="+id+"&plazas="+plazas,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				cargarListado("");
				}
			else
				{
				alert("Error al cambiar el número de plazas: "+respObj["resultado"]);
				}
		}
});
}
}

function guardarObservaciones(id)
{
var datos=JSON.parse("{}");
var contenido=encodeURI(tinymce.get('txt-'+id).getContent());
datos["contenido"]=contenido;
datos["id"]=id;
datos=JSON.stringify(datos);
$.ajax({
		url:"/Ezellohar/admin/plazas/CambiarObservacionesHabitacion",
		type:"POST",
		data:datos,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				alert("Observaciones actualizadas");
				}
			else
				{
				alert("Error al cambiar las observaciones: "+respObj["resultado"]);
				}
		}
});

}
function editObservaciones(id)
{
$("#notas-"+id).toggle();
	tinymce.init({
		  selector: 'textarea#txt-'+id,  // change this value according to your HTML
		  plugins: 'a_tinymce_plugin',
		  menubar:'save edit',
		  convert_urls:false,
		  a_plugin_option: true,
		  a_configuration_option: 400
		});

}

function ejecutarTarea()
{
var tarea=$('#tarea option:selected').val();
if (tarea=="eliminar")
{
eliminarPlazas();
}
else if (tarea=="bloquear")
{
estadoPlazasSeleccionadas(8);
estadoPlazasParcialesSeleccionadas(8);
}
else if (tarea=="reservar")
{
estadoPlazasSeleccionadas(3);
estadoPlazasParcialesSeleccionadas(3);
}
else if (tarea=="habilitar")
{
estadoPlazasSeleccionadas(1);
estadoPlazasParcialesSeleccionadas(1);
}
else if (tarea=="deshabilitar")
{
estadoPlazasSeleccionadas(0);
estadoPlazasParcialesSeleccionadas(0);
}
else if (tarea=="individual")
{
fraccionarPlazas();
}
else if (tarea=="recombinar")
{
recombinarPlazas();
}
}

function cargarListado()
{
	cargarListado("");
}


function editPrecioA(id,precio)
{
var precioNuevo=window.prompt("Introduzca el nuevo precio unitario para adultos en esta habitación:",precio);
if (precioNuevo!=null)
{
$.ajax({
		url:"/Ezellohar/admin/plazas/CambiarPrecioAdultoHabitacion",
		type:"POST",
		data:"id="+id+"&precioA="+precioNuevo,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				cargarListado("");
				}
			else
				{
				alert("Error al cambiar precio unitario para adultos: "+respObj["resultado"]);
				}
		}
});
}
}

function editPrecioM(id,precio)
{
var precioNuevo=window.prompt("Introduzca el nuevo precio unitario para menores   en esta habitación:",precio);
if (precioNuevo!=null)
{
$.ajax({
		url:"/Ezellohar/admin/plazas/CambiarPrecioMenorHabitacion",
		type:"POST",
		data:"id="+id+"&precioM="+precioNuevo,
		success:function(respuesta,estados){
			var respObj=JSON.parse(respuesta);
			if (respObj["resultado"]=="ok")
				{
				cargarListado("");
				}
			else
				{
				alert("Error al cambiar precio unitario para menores: "+respObj["resultado"]);
				}
		}
});
}
}

function verOcupantes(idh)
{
$("#ocupantes-"+idh).toggle();
}

function formatearOcupantes(lista)
{
var listado="";
if (typeof lista != 'undefined')
{for (var i=0;i<lista.length;i++)
{
	listado+=lista[i]+"<br>";
}
}
return listado;
}

function filtrarEstados()
{

var opcion=$("#estados").find('option:selected').val();
if (opcion=="todos")
{
$('tr[class*="clase"]').show();
}
else
{
$('tr[class*="clase"]').hide();
if (opcion=="Disponible")
{
$('tr[class*="claseDisponible"]').show();
}
if (opcion=="Desactivada")
{
$('tr[class*="claseDesactivada"]').show();
}
if (opcion=="Reservada")
{
$('tr[class*="claseReservada"]').show();
}
if (opcion=="Bloqueada")
{
$('tr[class*="claseBloqueada"]').show();
}
if (opcion=="Temporal")
{
$('tr[class*="claseTemporal"]').show();
}
if (opcion=="Parcial")
{
$('tr[class*="claseParcial"]').show();
}
}

}
function comboFiltroEstados()
{
var combo="<select id=\"estados\" onChange=\"filtrarEstados();\">";
combo+="<option value=\"todos\">Todos los estados</option>";
combo+="<option value=\"Disponible\">Disponibles</option>";
combo+="<option value=\"Desactivada\">Desactivadas</option>";
combo+="<option value=\"Reservada\">Reservadas</option>";
combo+="<option value=\"Bloqueada\">Bloqueadas</option>";
combo+="<option value=\"Temporal\">Bloqueo temporal</option>";
combo+="<option value=\"Parcial\">Inscripciones parciales</option>";
combo+="</select>";
return combo;
}

function verOcupantes(id)
{

$.ajax({
		url:"/Ezellohar/admin/plazas/VerOcupantes",
		data:"idHabitacion="+id,
		type:"GET",
		success:function(respuesta,estados){
		$("#ocupantesListado-"+id).html(respuesta);
		$("#ocupantesListado-"+id).show();
		}
		});
}

function cargarListado(opciones)
{

$.ajax({
		url:"/Ezellohar/admin/plazas/ListPlazas",
		data:"orden="+opciones,
		type:"GET",
		success:function(respuesta,estados){
			var listado=JSON.parse(respuesta);
			var linea="";
			var esdoble="";
			var contenido="";
			var estadoStr="";
			var estadoClass="";
			if (listado.length>0)
			{
			contenido="<table class=\"tablaDatos\" style=\"width:100%\">";
			contenido+="<thead><tr><th></th><th>Num.</th><th>Id</th><th>Capacidad</th><th>Ocupación</th><th>Planta</th><th>Nombre</th><th>"+comboFiltroEstados()+"</th><th>Precio Adultos</th><th>Precio Menores</th></tr></thead>";
				for (var i=0;i<listado.length;i++)
					{
					var idh=listado[i]["id"];
					var estado=listado[i]["estado"];
					var ocupantes=listado[i]["ocupantes"];
					if (estado==0)
					{
						estadoStr="<b>Desactivada</b>";
						estadoClass="claseDesactivada";
					}
					else if (estado==1)
					{
						estadoStr="<b>Disponible</b>";
						estadoClass="claseDisponible";
					}
					else if (estado==2)
					{
						estadoStr="<b>Temporal</b>";
						estadoClass="claseTemporal";
					}
					else if (estado==3)
					{
							estadoStr="<b>Reservada <a href=\"javascript:verOcupantes('"+idh+"');\">(+)</a></b><div id=\"ocupantes-"+idh+"\" style=\"display:none\">"+formatearOcupantes(ocupantes)+"</div>";
							estadoClass="claseReservada";
					}
					else if (estado==4)
					{
						estadoStr="<b>Pagada</b>";
						estadoClass="clasePagada";
					}
					else if (estado==9)
					{
						estadoStr="<b>Habilitada para Inscr. Individuales</b>";
						estadoClass="claseParcial";
					}
					else if (estado==8)
					{
						estadoStr="<b>Bloqueada</b>";
						estadoClass="claseBloqueada";
					}
					var estilo="";
					var plazas=listado[i]["plazas"]
					var camas = listado[i]["camas"];
					var ocupacion = listado[i]["ocupacion"];
					var precioA = listado[i]["precioAdultos"];
					var precioM = listado[i]["precioMenores"];
					if ((ocupacion!=plazas) && (estado==3 || estado==4)) estilo="style=\"background-color:red\"";
					var nombre=listado[i]["identificador"];
					var planta=listado[i]["planta"];
					if (planta=="") planta="(sin asignar)";
					if (nombre=="") nombre="(sin nombre)";
					
					if (estado==9)
					{
					linea="<tr class=\""+estadoClass+"\"><td><input class='seleccion-raiz' type='checkbox' id='chk-"+idh+"' value='"+idh+"' disabled></td>";
					linea+="<td><b>"+(i+1)+".-</b></td><td>"+idh+"</td><td>"+listado[i]["plazas"]+" personas ("+camas+" camas)</td><td "+estilo+">"+ocupacion+"</td><td><a href='javascript:asignarPlanta(\""+idh+"\",\""+planta+"\");'>"+planta+"</a></td><td><a href='javascript:asignarNombre(\""+idh+"\",\""+nombre+"\");'>"+nombre+"</a></td><td>"+estadoStr+"</td>";
					}
					else
					{
					linea="<tr class=\""+estadoClass+"\"><td><input class='seleccion' type='checkbox' id='chk-"+idh+"' value='"+idh+"'></td>";
					linea+="<td><b>"+(i+1)+".-</b></td><td>"+idh+"</td><td><a href='javascript:cambiarPlazas(\""+idh+"\",\""+listado[i]["plazas"]+"\");'>"+listado[i]["plazas"]+" personas</a> (<a href='javascript:cambiarCamas(\""+idh+"\",\""+camas+"\");'>"+camas+" camas</a>)</td><td "+estilo+">"+ocupacion+"</td><td><a href='javascript:asignarPlanta(\""+idh+"\",\""+planta+"\");'>"+planta+"</a></td><td><a href='javascript:asignarNombre(\""+idh+"\",\""+nombre+"\");'>"+nombre+"</a></td><td>"+estadoStr+"</td>";
					}


					linea+="<td><a href=\"javascript:editPrecioA('"+idh+"',"+precioA+");\">"+precioA+"</a></td><td><a href=\"javascript:editPrecioM('"+idh+"',"+precioM+");\">"+precioM+"</a></td><td><a href=\"javascript:editObservaciones('"+idh+"');\">Editar observaciones</a> <a href=\"javascript:verOcupantes('"+idh+"');\">Cargar ocupantes</a></td></tr>";
					
					linea+="<tr class=\""+estadoClass+"\"><td style=\"border:0px;\" colspan=7><div id=\"notas-"+idh+"\" class=\"observaciones\"><textarea id=\"txt-"+idh+"\">"+listado[i]["observaciones"]+"</textarea><input type=\"button\" onClick=\"guardarObservaciones('"+idh+"');\" value=\"Guardar\"></div></td></tr>";
					if (estado==9)
					{
					linea+="<tr class=\""+estadoClass+"\"><td colspan=7><div class=\"div-parcial-raiz\"><a id=\"show-"+idh+"\" href=\"javascript:$('#divparcial-"+idh+"').toggle();\" style=\"display:block\">Ver/Ocultar plazas individuales</a><div class=\"div-parcial\" id=\"divparcial-"+idh+"\" style=\"display:none\"></div></div></td></tr>";
					}
					linea+="</tr>";
					linea+="<tr class=\""+estadoClass+"\"><td style=\"border:0px;\" colspan=7><div id=\"ocupantesListado-"+idh+"\" class=\"observaciones\" style=\"font-family:Arial\"></div></td></tr>";
					contenido+=linea;
					}
				$('#listado').html(contenido);
				cargarListadoParciales();
			}
			else
				{
				$('#listado').html("<H2>No hay habitaciones dadas de alta en el sistema.</H2>");
				}
		}
});

}
function cargarListadoParciales()
{

$.ajax({
		url:"/Ezellohar/admin/plazas/ListPlazasParciales",
		type:"GET",
		success:function(respuesta,estados){
			var listado=JSON.parse(respuesta);
			var linea="";
			var esdoble="";
			var contenido="";
			var estadoStr="";
			var idRaiz="";
			var idRaizAnt="";
			var estadoClass="";
			if (listado.length>0)
			{
				for (var i=0;i<listado.length;i++)
					{
					var idh=listado[i]["idHabitacion"];
					var estado=listado[i]["estado"];
					var ocupantes=listado[i]["ocupantes"];
					idRaiz=listado[i]["idRaiz"];
					if (idRaiz!=idRaizAnt) 
					{
						$('#divparcial-'+idRaizAnt).html($('#divparcial-'+idRaizAnt).html()+linea);
						linea="";
						idRaizAnt=idRaiz;
					}
					if (estado==0)
					{
						estadoStr="<b>Desactivada</b>";
						estadoClass="claseParcialDesactivada";
					}
					else if (estado==1)
					{
						estadoStr="<b>Disponible</b>";
						estadoClass="claseParcialDisponible";
					}
					else if (estado==2)
					{
						estadoStr="<b>Temporal</b>";
						estadoClass="claseParcialTemporal";
					}
					else if (estado==3)
					{
						estadoStr="<b>Reservada <a href=\"javascript:verOcupantes('"+idh+"');\">(+)</a></b><div id=\"ocupantes-"+idh+"\" style=\"display:none\">"+formatearOcupantes(ocupantes)+"</div>";
						estadoClass="claseParcialReservada";
					}
					else if (estado==4)
					{
						estadoStr="<b>Pagada</b>";
						estadoClass="claseParcialPagada";
					}
					else if (estado==8)
					{
						estadoStr="<b>Bloqueada</b>";
						estadoClass="claseParcialBloqueada";
					}

					linea+="<input class='seleccionparcial' type='checkbox' id='chk-"+idh+"' value='"+idh+"'>";
			
					linea+=idh+": "+estadoStr+"<br>";
					}
					if (linea!="") 
					{
						$('#divparcial-'+idRaizAnt).html($('#divparcial-'+idRaizAnt).html()+linea);
						linea="";
						idRaizAnt=idRaiz;
					}
			}

		}
});
	
}