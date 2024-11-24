/**
 * 
 */


function cargarNoticia()
{
var id=location.href.split("?")[1];
$.ajax({
		url:"/Valimar/ViewNoticia",
		type:"GET",
        data:id,
		success:function(respuesta,estados){
			var listado=JSON.parse(respuesta);
			var linea="";
			var contenido="";

					var fecha=listado["fecha"];
					var fechaUpdate=listado["fechaUpdate"];
					linea="<div class=\"titulonoticia\"><span class=\"fecha\">"+fecha+"</span> <span >"+listado["titulo"]+"</span></div>";
					linea+="<div class=\"cuerponoticia\" id=\"noticia-"+listado["idNoticia"]+"\">"+listado["cuerpo"]+"</div>";
					contenido+=linea;

				$('#noticias').html(contenido);

		}
});
	
}


function cargarListado()
{

$.ajax({
		url:"/Valimar/ListNoticiasPublish",
		type:"GET",
		success:function(respuesta,estados){
			var listado=JSON.parse(respuesta);
			var linea="";
			var contenido="";
			if (listado.length>0)
			{
				for (var i=0;i<listado.length;i++)
					{
					var fecha=listado[i]["fecha"];
					var fechaUpdate=listado[i]["fechaUpdate"];
					linea="<div class=\"titulont\"><span class=\"fecha\">"+fecha+"</span><br><span class\"titulont\"><a style=\"cursor:pointer\" class=\"titulont\" onClick=\"$('#noticia-"+listado[i]["idNoticia"]+"').toggle();\">"+listado[i]["titulo"]+"</a></span></div>";
					linea+="<div class=\"cuerponoticia\" style=\"display:block\" id=\"noticia-"+listado[i]["idNoticia"]+"\"><a style=\"font-size: 11px\" href=\"viewpost.html?id="+listado[i]["idNoticia"]+"\" target=_blank>(Abrir noticia en otra ventana)</a><br>"+listado[i]["cuerpo"]+"</div>";
					contenido+=linea;
					}
				$('#noticias').html(contenido);
			}
			else
				{
				$('#noticias').html("<H2>Estamos trabajando en ello...</H2>");
				}
		}
});
	
}

function arrancarReloj(tiempo)
{
try
{
	var countdown = tiempo; 
	FlipClock.Lang.Custom = { days:'D&iacute;as', hours:'Horas', minutes:'Minutos', seconds:'Segundos' };
	var opts = {
		clockFace: 'DailyCounter',
		countdown: true,
		language: 'Custom'
	};
	opts.classes = {
		active: 'flip-clock-active',
		before: 'flip-clock-before',
		divider: 'flip-clock-divider',
		dot: 'flip-clock-dot',
		label: 'flip-clock-label',
		flip: 'flip',
		play: 'play',
		wrapper: 'flip-clock-small-wrapper'
	};  

	countdown = Math.max(1, countdown);
	$('.clock-builder-output').FlipClock(countdown, opts);
	}
	catch(e)
	{}
}



function generarMenu()
{
$.ajax({
		url:"/Valimar/CheckStatusFrontend",
		type:"POST",
		success:function(respuesta,estados){
		var opciones="<a class=\"box_button\"href=\"inicio.html\">Inicio</a>";
		opciones+="<a class=\"box_button\"href=\"mereth.html\">La Mereth Aderthad</a>";
		if (respuesta["invitados"]) opciones+="<a class=\"box_button\"href=\"invitados.html\">Los Invitados</a>";
		if (respuesta["inscripciones"]) opciones+="<a class=\"box_button\"href=\"inscripciones.html\">Inscripciones</a>";
		opciones+="<a class=\"box_button\"href=\"actividades.html\">Actividades</a>";
		if (respuesta["inscritos"]) opciones+="<a class=\"box_button\"href=\"/Formenos/login.html\" target=_blank>Zona de inscritos</a>";
		if (respuesta["punto_morado"]) opciones+="<a class=\"box_button\"href=\"onoresse.html\" style=\"color:pink\">Punto Morado</a>";
	if (!respuesta["formulario"])
	{
	if (respuesta["cuenta"]>0)
	{
	$("#accesoInscripcion").html("El formulario de inscripci&oacute;n estar&aacute; disponible en:");
	arrancarReloj(respuesta["cuenta"]);
	$("#cuentaatras").show();
	}
	}
	else
	{
	$("#accesoInscripcion").html("<a href=\"/Valimar/\" target=_blank>Acceder al Formulario de inscripci&oacute;n</a>");
	$("#cuentaatras").hide();
	}
	$('#botonesMenu').html(opciones);

		}
});
	
}


