/**
 * 
 */

function sala(nombre)
{
	$("#nombreSala").val(nombre);
	var direccion="../mapa/img/sala_"+nombre+".jpg";
	if (nombre=="Plano")
	{
		$("#mapaFondo").attr("src","../mapa/img/plano.jpg");
	}
	else
	{
			$("#mapaFondo").attr("src",direccion);
	}
	
	$("#mapaFondo").show();
}

function actividadesSala()
{
	var sala=$("#nombreSala").val();
	if (sala=="Plano")
	{
		alert("Selecciona una sala primero");
	}	
	else
	{
		cargarActividades(sala);
	}

	
}

function ocultarActividadesSala()
{
	var salaStr=$("#nombreSala").val();
			$("#actividadesSala").html("");
			$("#actividadesSala").hide();	
	sala(salaStr);

	
}

function cargarActividades(nombre)
{
$("#actividadesSala").show();
$("#actividadesSala").html("Cargando actividades de la sala, un momento...");
$("#mapaFondo").hide();
	$.ajax({
		url:"/MerethBackendPublic/ActividadesSala",
		type:"POST",
        data:"sala="+nombre,
		success:function(respuesta,estados){
			
			$("#actividadesSala").html(respuesta);
		}
});
}

function ocultarActividades(nombre)
{
			$("#actividadesSala").html("");
			$("#actividadesSala").hide();
			$("#mapaFondo").show();
			
}