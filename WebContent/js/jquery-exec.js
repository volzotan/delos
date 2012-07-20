// Sicherheitslevel des Passwort
$(window).load(function() {
	if(!$(':password.secure-check').empty()) {
		$().secureCheck($(':password.secure-check'));
		$().equalCheck($(':password.equal-check'));
	}
	if (0 < $('.hlResult').val().length) {
		$('td.hlTarget').highlight($('.hlResult').val());
	}
});

$(':password.secure-check').keyup(function() {
	$().secureCheck($(this));
});

$(':password.equal-check').keyup(function() {
	$().equalCheck($(this));
});

jQuery.fn.extend({
	secureCheck: function (element) {		
		console.log(element);
		var pwLength = element.val().length;
		var w = -940 + (((element.outerWidth() - 2) / 6) * pwLength);
		if(pwLength < 7) {
			element.css({ backgroundPosition : w + 'px 0' });
		} else {
			element.css({ backgroundPosition : '0 0' });
		}
		if(pwLength > 5) {
			if(pwLength < 8) {
				element.removeClass('good').addClass('ok');
			} else {
				element.removeClass('ok').addClass('good');
			}
		} else {
			element.removeClass('ok').removeClass('good');
		}
		$().equalCheck(element.parents('form').find(':password.equal-check'));
	}
});

jQuery.fn.extend({
	equalCheck: function (element) {
		var pwFirst = element.parents('form').find(':password.secure-check').val();
		var w = -940 + (((element.outerWidth() - 2) / pwFirst.length) * element.val().length);
		if(pwFirst.length > 0) {
			if(element.val().length <= pwFirst.length) {
				element.css({ backgroundPosition : w + 'px 0' });
			}
			if(element.val() == pwFirst && pwFirst.length > 5) {
				element.addClass('good');
			} else {
				element.removeClass('good');
			}
		} else {
			element.css({ backgroundPosition : '-940px 0' });
		}
	}
});

// Platzhalter für IE/Firefox und ältere Browser
$('[placeholder]').focus(function() {
	var input = $(this);
	if(input.val() == input.attr('placeholder')) {
		input.val('');
		input.removeClass('placeholder');
	}
}).blur(function() {
	var input = $(this);
	if(input.val() == '' || input.val() == input.attr('placeholder')) {
		input.addClass('placeholder');
		input.val(input.attr('placeholder'));
	}
}).blur().parents('form').submit(function() {
	$(this).find('[placeholder]').each(function() {
		var input = $(this);
		if(input.val() == input.attr('placeholder')) {
			input.val('');
		}
	});
});

// Toggle der Popover's
$('.popover-toggle').click(function() {
	poHover = false;
	toggle = $(this);
	popover = $('div.popover.' + toggle.attr('id'));
	toggle.toggleClass('active');
	popover.toggleClass('active');
	$(document).on('click', function() {
		popover.mouseover(function() { poHover = true; }).mouseout(function() { poHover = false; });
		if(!poHover) {
			toggle.removeClass('active');
			popover.removeClass('active');
		}
	});
	return false;
});

// Autofocus
$('input.autofocus').focus();

// "Just Type"-Funktion, Suchfeldfunktionalität und... was zur... was ist das?!
$(function() {
	var kkeys = [];
	var skeys = [17, 18, 91, 92, 93];
	var konami = "38,38,40,40,37,39,37,39,66,65";
	var nB = $('.navbar-search');
	var sI = $('.icon-search');
	var sF = $('.search-field');
	var barActive = false;
	var barFocused = false;
	var iconHover = false;
	var isSysKey = false;
	
	$(document).keydown(function(e) {
		if($.inArray(e.keyCode, skeys) > -1) {
			isSysKey = true;
		}
		kkeys.push(e.keyCode);
		if(kkeys.toString().indexOf(konami) >= 0) {
			$(document).unbind('keydown', arguments.callee);
			$.getScript('http://www.cornify.com/js/cornify.js', function() {
				cornify_add();
				$(document).keydown(cornify_add);
			});    
		} else if(kkeys.toString().indexOf("38,38,40,40,37,39,37,39,66") >= 0) {
			barFocused = true;		
		}
		if(!$(':input').is(':focus') && !barFocused && !isSysKey) {
			if(e.keyCode >= 49 && e.keyCode <= 90) {
				if(barActive == false) {
					sF.focus().addClass("active");;
					sI.addClass("active");
					barActive = true;
					barFocused = true;
				}
				if(sF.val().trim() != "" && sF.val() != null) {
					sF.val("").focus().stop().animate({ width : "210px" }, 200);
					barFocused = true;
				}
			}
		}
	}).keyup(function(e) {
        if($.inArray(e.keyCode, skeys) > -1) {
        	isSysKey = false;
        }
    });
	
	sI.hover(function() {iconHover = true;}, function() {iconHover = false;});
	nB.focusout(function() {
		if(!iconHover) {
			if(sF.val().trim() == "" || sF.val() == null) {
				$(sI, this).removeClass("active");
				$(sF, this).val("").removeClass("active");
				barActive = false;
			}
			$(sF, this).val(sF.val().trim()).stop().animate({ width : "138px" }, 200);
			barFocused = false;
		}
	});
	nB.focusin(function() {
		if(barActive == false) {
			sI.addClass("active");
			sF.addClass("active");
			barActive = true;
			sF.stop().animate({ width : "210px" }, 200);
		} else {
			if(sF.val().trim() != "" && sF.val() != null) {
				sF.stop().animate({ width : "210px" }, 200);
			}
		}
		barFocused = true;
	});
});

// Semester- und Heimadresse identisch Button toggle
$(function() {
	var button = $('button.adresseIdentischToggle');
	var form = button.parents('form');
	var strasse, hausnummer, plz, stadt, land;
	
	button.on('click', function() {
		strasse = form.find('#Benutzer_registrieren_benutzer_strasse');
		hausnummer = form.find('#Benutzer_registrieren_benutzer_hausnummer');
		plz = form.find('#Benutzer_registrieren_benutzer_postleitzahl');
		stadt = form.find('#Benutzer_registrieren_benutzer_stadt');
		land = form.find('#Benutzer_registrieren_benutzer_land');
		
		if(!button.hasClass('active')) {
			form.find('#Benutzer_registrieren_benutzer_strasseHeimat').attr('readonly', 'readonly');
			form.find('#Benutzer_registrieren_benutzer_hausnummerHeimat').attr('readonly', 'readonly');
			form.find('#Benutzer_registrieren_benutzer_postleitzahlHeimat').attr('readonly', 'readonly');
			form.find('#Benutzer_registrieren_benutzer_stadtHeimat').attr('readonly', 'readonly');
			form.find('#Benutzer_registrieren_benutzer_landHeimat').attr('readonly', 'readonly');
			
			toggleHeimatInput(strasse.val(), hausnummer.val(), plz.val(), stadt.val());
			toggleHeimatSelect(land.val());
		} else {
			form.find('#Benutzer_registrieren_benutzer_strasseHeimat').removeAttr('readonly');
			form.find('#Benutzer_registrieren_benutzer_hausnummerHeimat').removeAttr('readonly');
			form.find('#Benutzer_registrieren_benutzer_postleitzahlHeimat').removeAttr('readonly');
			form.find('#Benutzer_registrieren_benutzer_stadtHeimat').removeAttr('readonly');
			form.find('#Benutzer_registrieren_benutzer_landHeimat').removeAttr('readonly');
		}
	});
	
	$('input, select', form).keyup(function() {
		if(button.hasClass('active')) {
			strasse = form.find('#Benutzer_registrieren_benutzer_strasse').val();
			hausnummer = form.find('#Benutzer_registrieren_benutzer_hausnummer').val();
			plz = form.find('#Benutzer_registrieren_benutzer_postleitzahl').val();
			stadt = form.find('#Benutzer_registrieren_benutzer_stadt').val();
			land = form.find('#Benutzer_registrieren_benutzer_land').val();
			
			toggleHeimatInput(strasse, hausnummer, plz, stadt);
			toggleHeimatSelect(land);
		}
	});
	
	$('select', form).on('click', function() {
		if(button.hasClass('active')) {
			land = form.find('#Benutzer_registrieren_benutzer_land').val();
			
			toggleHeimatSelect(land);
		}
	});
	
	function toggleHeimatSelect(land) {		
		form.find('#Benutzer_registrieren_benutzer_landHeimat').val(land);
	}
	
	function toggleHeimatInput(strasse, hausnummer, plz, stadt) {		
		form.find('#Benutzer_registrieren_benutzer_strasseHeimat').val(strasse);
		form.find('#Benutzer_registrieren_benutzer_hausnummerHeimat').val(hausnummer);
		form.find('#Benutzer_registrieren_benutzer_postleitzahlHeimat').val(plz);
		form.find('#Benutzer_registrieren_benutzer_stadtHeimat').val(stadt);
	}
});

// AGB und DSB Link in Label
$('.agb-check').find('label.checkbox').append('<a href="Seite_nutzungsbedingungen.action" target="_blank"><i class="icon-question-sign icon-fade"></i></a>');


// Mehrfachwahl von Checkboxen in Formularen
//$(function() {
//	var leftButtonDown = false;
//	var rightButtonDown = false;
//    $(document).mousedown(function(e){
//        if(e.which === 1) { 
//        	leftButtonDown = true; 
//        } else if(e.which === 3) {
//        	rightButtonDown = true;
//        }
//    }).mouseup(function(e){
//     	leftButtonDown = false; 
//     	rightButtonDown = false;
//    });
//    $(':checkbox').mouseenter(function() {
//    	if(leftButtonDown && !rightButtonDown) {
//    		$(this).attr('checked', true);
//    	} else if(rightButtonDown && !leftButtonDown) {
//    		$(this).attr('checked', false);
//    	}
//    });
//});

$.fn.selectAllBoxes = function() {
	$(this).parents('form').find(':checkbox').attr('checked', true);
};

$.fn.selectNoneBoxes = function() {
	$(this).parents('form').find(':checkbox').attr('checked', false);
};

$.fn.invertSelection = function() {
	$(this).parents('form').find(':checkbox').each(function() {
		if($(this).is(':checked')) {
			$(this).attr('checked', false);
		} else {
			$(this).attr('checked', true);
		}
	});
};

// Nachrichten spezifische Mehrfachauswahl von Checkboxen in Formularen
$.fn.selectReadBoxes = function() {
	$(this).parents('form').find('tr').each(function() {
		if($(this).hasClass('status-highlight')) {
			$(':checkbox', this).attr('checked', false);
		} else {
			$(':checkbox', this).attr('checked', true);
		}
	});
};

$.fn.selectUnreadBoxes = function() {
	$(this).parents('form').find('tr').each(function() {
		if($(this).hasClass('status-highlight')) {
			$(':checkbox', this).attr('checked', true);
		} else {
			$(':checkbox', this).attr('checked', false);
		}
	});
};

$.fn.selectAnsweredBoxes = function() {
	$(this).parents('form').find('tr').each(function() {
		if($('i', this).hasClass('icon-share-alt')) {
			$(':parent :checkbox', this).attr('checked', true);
		} else {
			$(':parent :checkbox', this).attr('checked', false);
		}
	});
};

// Verlinkung von ganzen Zeilen in Tabellen unter Ausschluss von Checkboxen
$('tr.hyperlink td').not('.filter').on('click', function() {
	window.location.href = $(this).parent().attr('data-toggle');
});

// Vertreter Hinweis
$('.vertreter-info a').mouseenter(function() {
	var v = $(this).parent();
	var t = setTimeout(function() {
		$('.hinweis', v).css('display', 'none');
		$('.beenden', v).css('display', 'block');
	}, 333);
	v.mouseleave(function() {
		clearTimeout(t);
		$('.beenden', v).css('display', 'none');
		$('.hinweis', v).css('display', 'block');
	});
});

$(function() {
	var matInput = $('.input-matrikel');
	if (matInput.val() == 0) {
		matInput.val('');
	}
});

//Tooltip
$('.show-tooltip').tooltip();
$('.show-tooltip-bottom').tooltip({
	placement : 'bottom'
});
$('.show-tooltip-left').tooltip({
	placement : 'left'
});
$('.show-tooltip-right').tooltip({
	placement : 'right'
});

// Fernauslösung des Tooltips
$('.trigger-tooltip').not('.show-tooltip:parent').mouseenter(function() {
	$('.show-tooltip', this).tooltip().triggerHandler('mouseenter');
}).mouseleave(function() {
	$('.show-tooltip', this).triggerHandler('mouseleave');
});

// Modal
$('.modal').modal('hide');

// Popover
$('.popover-bottom').popover({
	placement: 'bottom',
});
$('.popover-top').popover({
	placement: 'top',
});
$('.popover-left').popover({
	placement: 'left',
});
$('.popover-right').popover({
	placement: 'right',
});

// BenutzerTour Carousel
$('.carousel').carousel({
    interval: 6666
});

// Kontrolliertes Carousel
$(function() {
	var cc = $('#controlledSlide.carousel');
	cc.carousel('pause');
	$('.slideToNextItem', cc).on('click', function() {
		cc.carousel('next');
	});
	$('.slideToPrevItem', cc).on('click', function() {
		cc.carousel('prev');
	});
});

// WYSIHTML5 Config
//$('#BewerbungsVorgang_erstellen, #BewerbungsVorgang_aktualisieren, #Dokument_aktualisieren, #Dokument_erstellen, #Ausschreibung_aktualisieren, #Ausschreibung_erstellen, #Nachricht_erstellen, #Nachricht_antworten').wysihtml5({
$('textarea').wysihtml5({
	"font-styles": false, //Font styling, e.g. h1, h2, etc. Default true
	"emphasis": true, //Italics, bold, etc. Default true
	"lists": true, //(Un)ordered lists, e.g. Bullets, Numbers. Default true
	"html": false, //Button which allows you to edit the generated HTML. Default false
	"link": true, //Button to insert a link. Default true
	"image": false //Button to insert an image. Default true
});