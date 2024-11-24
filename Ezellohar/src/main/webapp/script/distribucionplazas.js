/**
 * 
 */
function cargarDatos()
{
	$("#distribucionTemporal").val("{}");
	cargarCSV("#fichero-distribucion","#destino");
}


function formatearUbicaciones(listado)
{
	var contenido="";
var lineas = listado.split(/[\r\n]+/g);
var habitacion="";
var id;
var plazas;
var camas;
var planta;
var obs;
var tam;
for(var i = 0; i < lineas.length; i++)
{
	habitacion=lineas[i];
	id=habitacion.split(";")[0];
	planta=habitacion.split(";")[1];
	plazas=habitacion.split(";")[2];
	camas=habitacion.split(";")[3];
	obs=habitacion.split(";")[4];
	tam=habitacion.split(";")[5];
	contenido+="<div id='"+id+"' camas='"+camas+"' plazas='"+plazas+"' planta='"+planta+"' style='margin:5px;padding:5px;border-width:3px;border-radius:10px;border-style:solid;width:320px' onClick='toggleSelectOrigen("+id+","+plazas+","+camas+","+planta+");'>";
	contenido+="Habitación: "+id+"<br>";
	contenido+="(Planta: "+planta+" Plazas:"+plazas+" Camas:"+camas+" "+obs+" Tipo cama: "+tam+")";
	contenido+="</div>";
}
return contenido;
}

function toggleSelectOrigen(id,plazas,camas,planta)
{
	if ($("#"+id).attr("lock")!="true")
	{
	if ($("#ubicacionSeleccionada").val()=="")
	{
		$("#ubicacionSeleccionada").val(id);
		$("#plantaSeleccionada").val(planta);
		$("#"+id).css('background-color','CornflowerBlue');
	}
	else
	{
		if ($("#ubicacionSeleccionada").val()==id)
		{
			$("#ubicacionSeleccionada").val("");
			$("#plantaSeleccionada").val("");
		$("#"+id).css('background-color','white');
		}
	}
	controlarAsignacion();
	}
}

function toggleSelectDestino(id,plazas,camas)
{
if ($("#"+id).attr("lock")!="true")
	{
	if ($("#habitacionSeleccionada").val()=="")
	{
		$("#habitacionSeleccionada").val(id);
		$("#"+id).css('background-color','Beige');
	}
	else
	{
		if ($("#habitacionSeleccionada").val()==id)
		{
			$("#habitacionSeleccionada").val("");
		$("#"+id).css('background-color','white');
		}
	}
	controlarAsignacion();
	}
	else
	{
		var asignaciones=JSON.parse($("#distribucionTemporal").val());
		var habitacion=id;
		var ubicacion=asignaciones[habitacion]["ubicacion"];
		var planta=asignaciones[habitacion]["planta"];
		asignaciones[habitacion]["ubicacion"]="";
		asignaciones[habitacion]["planta"]="";
		$("#"+ubicacion).attr("lock","");
		$("#"+habitacion).attr("lock","");
		$("#distribucionTemporal").val(JSON.stringify(asignaciones));
		$("#"+habitacion).prependTo($("#habitaciones"));
		$("#"+habitacion).css('background-color','white');
	}
}

function controlarAsignacion()
{
	var ubicacion=$("#ubicacionSeleccionada").val();
	var habitacion=$("#habitacionSeleccionada").val();
	if (($("#habitacionSeleccionada").val()!="") && ($("#ubicacionSeleccionada").val()!="") && ($("#"+$("#ubicacionSeleccionada").val()).attr("lock")!="true"))
	{
		if (($("#"+$("#ubicacionSeleccionada").val()).attr("camas")==$("#"+$("#habitacionSeleccionada").val()).attr("camas")) && ($("#"+$("#ubicacionSeleccionada").val()).attr("plazas")==$("#"+$("#habitacionSeleccionada").val()).attr("plazas")))
		{
		var ubicacion=$("#ubicacionSeleccionada").val();
		var planta=$("#plantaSeleccionada").val();
		$("#"+$("#habitacionSeleccionada").val()).appendTo($("#"+$("#ubicacionSeleccionada").val()));
		var asignaciones=JSON.parse($("#distribucionTemporal").val());
		asignaciones[habitacion]["ubicacion"]=ubicacion;
		asignaciones[habitacion]["planta"]=planta;
		$("#distribucionTemporal").val(JSON.stringify(asignaciones));
		$("#"+ubicacion).attr("lock","true");
		$("#"+habitacion).attr("lock","true");
		$("#habitacionSeleccionada").val("");
		$("#ubicacionSeleccionada").val("");
		$("#plantaSeleccionada").val("");
		}
		else
		{
			alert("La capacidad de las habitaciones no coincide");
			$("#habitacionSeleccionada").val("");
			$("#ubicacionSeleccionada").val("");
			$("#plantaSeleccionada").val("");
			$("#"+ubicacion).css('background-color','white');
			$("#"+habitacion).css('background-color','white');
		}
	}
}


function asignacionenCarga()
{
	var distribucion=JSON.parse($("#distribucionTemporal").val());
	for (var d in distribucion)
	{
		var ubicacion=distribucion[d]["ubicacion"];
		ubicacion=ubicacion.replace(/\(/g,"");
		ubicacion=ubicacion.replace(/ /g,"");
		ubicacion=ubicacion.replace(/\)/g,"");
		var planta=distribucion[d]["planta"];
		var habitacion=d;
	if ((habitacion!="") && (ubicacion!="") && ($("#"+ubicacion).attr("lock")!="true"))
	{
		$("#"+habitacion).appendTo($("#"+ubicacion));
		var asignaciones=JSON.parse($("#distribucionTemporal").val());
		asignaciones[habitacion]["ubicacion"]=ubicacion;
		asignaciones[habitacion]["planta"]=planta;
		$("#distribucionTemporal").val(JSON.stringify(asignaciones));
		$("#"+ubicacion).attr("lock","true");
		$("#"+habitacion).attr("lock","true");
		if (($("#"+ubicacion).attr("camas")==$("#"+habitacion).attr("camas")) && ($("#"+ubicacion).attr("plazas")==$("#"+habitacion).attr("plazas")))
		{
			if (($("#"+habitacion).attr("vacia"))=="false")
			{
				$("#"+ubicacion).css('background-color','CornflowerBlue');
				$("#"+habitacion).css('background-color','Beige');
			}
			else
			{
			$("#"+ubicacion).css('background-color','DarkGrey');
			$("#"+habitacion).css('background-color','DarkSeaGreen');
			}
		}
		else
		{
			$("#"+ubicacion).css('background-color','red');
			$("#"+habitacion).css('background-color','red');
		}
	}
	}
}

function formateoListaOcupantes(ocupantes)
{
	var lista="";
	if (ocupantes.length==0)
		{
		return "(Habitación vacía)";	
		}
		else
		{
			for (var o=0;o<ocupantes.length;o++)
			{
				lista+=ocupantes[o]["nombre"]+" "+ocupantes[o]["apellidos"]+" ("+ocupantes[o]["pseudonimo"]+")<br>";
			}
			return lista;
		}
	
}


function formatearHabitaciones(listado)
{
	var distribucion=JSON.parse("{}");
	var contenido="";
	var identificador;
	var objeto;
	var planta="";
	var fondo="";
	for (var i=0;i<listado.length;i++)
	{
	if (planta!=listado[i]["planta"])
	{
	if (planta!="") 
		{
		contenido+="</div>";
		}
	 contenido+="<h2>Planta: "+listado[i]["planta"]+"</h2><div id=\"contenido-planta-"+listado[i]["planta"]+"\" style=\"display:flex;flex-wrap:wrap;flex-direction:row;background-color:DarkCyan;border:10px ridge rgba(121,121,121,0.205)\">";
	 planta=listado[i]["planta"];
	}
	
	var listaOcupantes=formateoListaOcupantes(listado[i]["ocupantes"]);
	var vacia=(listaOcupantes=="(Habitación vacía)");
	if (vacia)
	{
	fondo="DarkGrey";
	}
	else
	{
	fondo="DarkSeaGreen";
	}
		contenido+="<div id=\""+listado[i]["id"]+"\" plazas="+listado[i]["plazas"]+" vacia="+vacia+" camas="+listado[i]["camas"]+" planta="+listado[i]["planta"]+" style=\"margin:5px;padding:5px;border-width:3px;border-radius:10px;background-color:"+fondo+";border-style:solid;width:120px\">";
		contenido+="Código habitación: <b>"+listado[i]["identificador"]+"</b><br>Planta: "+listado[i]["planta"]+"<br>";
		contenido+="Plazas: "+listado[i]["plazas"]+" Camas:"+listado[i]["camas"]+"<br>";
		contenido+=listaOcupantes;
		identificador=listado[i]["id"];
		if (distribucion.hasOwnProperty(identificador))
		{
		objeto=distribucion[identificador];
		}
		else
		{
		objeto=JSON.parse("{}");
		}
		objeto["ubicacion"]=listado[i]["identificador"];
		objeto["planta"]=listado[i]["planta"];
		distribucion[listado[i]["id"]]=objeto;
		contenido+="</div>";
	}
	return contenido;
}
function formatearOcupantes(listado)
{
	var distribucion=JSON.parse("{}");
	var contenido="";
	var identificador;
	var objeto;
	for (var i=0;i<listado.length;i++)
	{
	
	var listaOcupantes=formateoListaOcupantes(listado[i]["ocupantes"]);
	var vacia=(listaOcupantes=="(Habitación vacía)");
		contenido+="<div id=\""+listado[i]["id"]+"\" plazas="+listado[i]["plazas"]+" vacia="+vacia+" camas="+listado[i]["camas"]+" planta="+listado[i]["planta"]+" style=\"margin:5px;padding:5px;border-width:3px;border-radius:10px;border-style:solid;width:320px\" onClick='toggleSelectDestino(\""+listado[i]["id"]+"\",\""+listado[i]["plazas"]+"\",\""+listado[i]["camas"]+"\");'>";
		contenido+="Código habitación: "+listado[i]["identificador"]+"<br>Planta: "+listado[i]["planta"]+"<br>";
		contenido+="Plazas: "+listado[i]["plazas"]+" Camas:"+listado[i]["camas"]+"<br>";
		contenido+=listaOcupantes;
		identificador=listado[i]["id"];
		if (distribucion.hasOwnProperty(identificador))
		{
		objeto=distribucion[identificador];
		}
		else
		{
		objeto=JSON.parse("{}");
		}
		objeto["ubicacion"]=listado[i]["identificador"];
		objeto["planta"]=listado[i]["planta"];
		distribucion[listado[i]["id"]]=objeto;
		contenido+="</div>";
	}
	$("#distribucionTemporal").val(JSON.stringify(distribucion));
	return contenido;
}

function cargarOcupantes()
{
	$("#aviso").html("Cargando habitaciones del sistema. Un momento por favor...");
	$.ajax({
		url:"/Ezellohar/ListPlazasOcupantes",
		type:"GET",
		success:function(respuesta,estados){
		var respObj=JSON.parse(respuesta);
		$("#habitaciones").html(formatearOcupantes(respObj));
		$("#aviso").html("");
		asignacionenCarga();
		}
});
	
}

function cargarPlano()
{
	$("#aviso").html("Cargando habitaciones del sistema. Un momento por favor...");
	$.ajax({
		url:"/Ezellohar/ListPlazasOcupantes",
		type:"GET",
		success:function(respuesta,estados){
		var respObj=JSON.parse(respuesta);
		$("#listado").html(formatearHabitaciones(respObj));
		$("#aviso").html("");
		}
});
	
}

function cargarArchivo(id)
{
	var input=document.getElementById(id);
	var archivo=input.files[0];
	fr=new FileReader();
	fr.onload=cargarCSV;
	fr.readAsText(archivo);
	}
	
	function cargarCSV(id)
	{

		$(id).on('change',function(){
			var fileReader=new FileReader();
			fileReader.onload=function()
			{
				var data=fileReader.result;
				$("#ubicaciones").html(formatearUbicaciones(data));
				cargarOcupantes();
			};
			fileReader.readAsText($(id).prop('files')[0],"ISO-8859-15");
			
		});
		
	}
function ejecutarTarea()
{
var tarea=$('#tarea option:selected').val();
if (tarea=="limpiar")
{
limpiarNombres();
}
else if (tarea=="asignar")
{
asignarNombres();
}
else
{
	alert("Selecciona una tarea primero");
}
}

function limpiarNombres()
{
	$.ajax({
		url:"/Ezellohar/admin/plazas/LimpiarNombres",
		type:"POST",
		success:function(respuesta,estados){
		alert(respuesta);
		}
});
}
function asignarNombres()
{
	var distribucion=JSON.parse($("#distribucionTemporal").val());
	var datos=JSON.parse("{}");
	for (var clave in distribucion)
	{
		if (distribucion[clave]["ubicacion"]!="")
		{
			datos[clave]=distribucion[clave];
		}
	}
	$.ajax({
		url:"/Ezellohar/admin/plazas/AsignarNombres",
		type:"POST",
		data:"datos="+JSON.stringify(datos),
		success:function(respuesta,estados){
		alert(respuesta);
		}
});
}	
