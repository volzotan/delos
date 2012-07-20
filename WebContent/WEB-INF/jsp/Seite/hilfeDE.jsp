<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>

<div class="row">
	<div class="span12">
		<div class="row">
			<div class="span9 offset3">
				<h1>Hilfe</h1>
				<hr />
			</div>
		</div>
		<div class="row">
			<div class="span3">
				<div class="well" style="padding: 8px 0; margin-top: -20px;">
					<ul class="nav nav-list">
						<li class="nav-header">Allgemein</li>
						<li><a href="#1">Was ist Delos?</a></li>
					<s:if test="#session.angemeldeterBenutzer.gruppeId == 2 || #session.angemeldeterBenutzer.gruppeId == 1">
						<li class="nav-header">Wie benutze ich Delos?</li>
						<li><a href="#2">Wie werde ich Hiwi?</a></li>
						<li><a href="#3">Ich habe mich beworben was jetzt?</a></li>
						<li><a href="#4">Wie ziehe ich meine Bewerbung wieder zurück?</a></li>
						<li><a href="#5">Was mache ich wenn meine Bewerbung angenommen wurde?</a></li>
					</s:if>
					<s:if test="#session.angemeldeterBenutzer.gruppeId == 3 || #session.angemeldeterBenutzer.gruppeId == 1">
						<li class="nav-header">Wie benutze ich Delos?</li>
						<li><a href="#6">Wie bekomme ich einen Hiwi?</a></li>
						<li><a href="#7">Wie erstelle/ändere ich eine Ausschreibung?</a></li>
						<li><a href="#8">Wie nehme ich Bewerbungen an oder lehne sie ab?</a></li>
						<li><a href="#9">Was passiert nachdem ich eine Bewerbung angenommen habe</a></li>
						<li><a href="#10">Wie benutze ich die Vertreterfunktion?</a></li>
					</s:if>	
					<s:if test="#session.angemeldeterBenutzer.gruppeId == 4 || #session.angemeldeterBenutzer.gruppeId == 1">
						<li class="nav-header">Wie benutze ich Delos?</li>
						<li><a href="#11">Wie fordere ich Dokumente an?</a></li>
						<li><a href="#12">Wie bestätige ich bereits eingegangene Dokumente?</a></li>
						<li><a href="#13">Wie schließe ich einen Bewerbungsvorgang ab?</a></li>
						<li><a href="#14">Wie benutze ich die Vertreterfunktion?</a></li>					
					</s:if>		
					</ul>
				</div>
			</div>
			<div class="span9">			
				<h3 id="1">Was ist Delos?</h3>
				<div class="well">
					Delos ist ein neues System, in dem Mitarbeiter der Universität Ulm Stellen ausschreiben und registrierte Benutzer
					sich darauf bewerben können.
				</div>
			<s:if test="#session.angemeldeterBenutzer.gruppeId == 2 || #session.angemeldeterBenutzer.gruppeId == 1">
				<h3 id="2">Wie werde ich Hiwi?</h3>
				<div class ="well">
					Um ein Hiwi zu werden, muss man von einem Mitarbeiter der Universität eingestellt werden. Nach einer Registrierung im
					System ist es möglich, sich auf die Ausschreibungen der Mitarbeiter zu bewerben und somit einer Hiwi-Stelle einen
					großen Schritt näher zu kommen.
				</div>
				<h3 id="3">Ich habe mich beworben, was jetzt?</h3>
				<div class="well">
					Nach deiner Bewerbung hat der Ausschreiber die Möglichkeit diese anzunehmen oder abzulehnen. Wurde die Bewerbung
					angenommen, müssen alle benötigten Dokumente beim zuständigen Bearbeiter eingereicht werden. Um welche Dokumente es
					sich handelt und wo diese hingebracht werden müssen kannst du in den Details zu deiner Bewerbung und dem Benutzerprofil
					des bearbeiters einsehen.
				</div>
				<h3 id="4">Wie ziehe ich meine Bewerbung wieder zurück</h3>
				<div class="well">
					Es ist natürlich jederzeit möglich eine Bewerbung wieder zurückzuziehen. Hierzu musst du einfach die Bewerbungsdetails
					aufrufen und auf "Zurückziehen" klicken.
				</div>
				<h3 id="5">Was mach ich wenn meine Bewerbung angenommen wurde?</h3>
				<div class="well">
					Wenn deine Bewerbung angenommen wurde und du alle nötigen Dokumente eingereicht hast, musst du zu deinem Bearbeiter gehen und
					dort einen Hiwi-Vertrag unterschreiben.
				</div>
			</s:if>
			<s:if test="#session.angemeldeterBenutzer.gruppeId == 3 || #session.angemeldeterBenutzer.gruppeId == 1">				
				<h3 id="6">Wie bekomme ich Hiwis?</h3>
				<div class="well">
					Wenn alle nötigen Genehmigungen für Hiwi-Stellen eingeholt wurden, haben Sie die Möglichkeit im System eine Ausschreibung
					zu erstellen. Studenten können sich als Bewerber registrieren und die Stellenausschreibungen aufrufen. Ist ein Student
					interessiert, bewirbt er sich und Sie werden darüber informiert. Dann können Sie entscheiden, ob Sie diese Bewerbung annehmen
					oder ablehnen möchten. Angenommene Bewerbungen werden an die Bearbeiter weitergeleitet um die nötigen Dokumente anzufordern
					und entgegenzunehmen. Hat der Student alle Dokumente eingereicht, kann er den Hiwi-Vetrag unterschreiben und für Sie arbeiten.
				</div>
				<h3 id="7">Wie erstelle/ändere ich eine Ausschreibung?</h3>
				<div class="well">
					Zum Erstellen, rufen sie zuerst die Ausschreibungsübersicht auf, indem sie auf "Jobs" klicken. Dort finden Sie einen Button
					um neue Ausschreibungen zu erstellen. Um Ihre Ausschreibung zu bearbeiten rufen Sie diese auf und klicken auf den entsprechenden
					Button. Sie können Ihre Ausschreibung hier nicht nur bearbeiten sondern auch löschen oder vorzeitig beenden.
				</div>
				<h3 id="8">Wie nehme ich Bewerbungen an oder lehne sie ab?</h3>
				<div class="well">
					Wenn sie in der Ausschreibungsübersicht den Reiter "Bewerbungen" auswählen, werden Ihnen alle Bewerbungen auf Ihre Ausschreibungen
					angezeigt. Rufen sie mit einem Doppelklick auf die Bewerbung die Detailansicht auf, dort können sie entscheiden ob Sie eine
					Bewerbung annehmen oder ablehnen möchten.
				</div>
				<h3 id="9">Was passiert nachdem ich eine Bewerbung angenommen habe?</h3>
				<div class="well">
					Haben Sie eine Bewerbung angenommen kann der zuständige Bearbeiter die nötigen Dokumente anfordern und entgegen nehmen. Hat
					der Bewerber alle Dokumente abgegeben kann er den Vertrag beim Bearbeiter unterschreiben und anschließend seine Tätigkeit bei
					Ihnen beginnen.
				</div>
				<h3 id="10">Wie benutze ich die Vertreterfunktion?</h3>
				<div class="well">
					In ihrem Profil können Sie die Email-Adresse eines Vertreters angeben, dieser muss allerdings auch im System registriert sein.
					Sollten Sie einmal ihre Ausschreibungen nicht selbst bearbeiten können kann ihr Vetreter über diese Funktion auf ihre Nachrichten
					und Ausschreibungen zugreifen und bei Bedarf bearbeiten. Um in den Vertretermodus zu wechseln klicken sie oben rechts auf ihren Namen
					und in dem Menü auf Vertretermouds.
				</div>
			</s:if>	
			<s:if test="#session.angemeldeterBenutzer.gruppeId == 4 || #session.angemeldeterBenutzer.gruppeId == 1">				
				<h3 id="11">Wie fordere ich Dokumente an?</h3>
				<div class="well">
					Die wichtigsten Dokumente werden automatisch angefordert. Sollten Sie allerdings noch weitere benötigen können Sie aus einer Liste, 
					von bereits vorhandenen Dokumenten, alle auswählen die der Bewerber zusätzlich abgeben soll. Rufen Sie dazu einfach die Detailansicht
					der Bewerbung auf für die Sie weitere Dokumente anfordern möchten, wählen sie dann "Dokumentenstatus bearbeiten" aus. Jetzt haben Sie
					nicht nur die Möglichkeit Dokumente als angenommen oder fehlend zu markieren sondern können über die Funktion "Weitere Dokumente hinzufügen"
					zusätzliche Dokumente anfordern
				</div>
				<h3 id="12">Wie bestätige ich bereits eingegangen Dokumente?</h3>
				<div class="well">
					Um eingegangene Dokumente zu bestätigen müssen Sie von der Detailansicht der Bewerbung aus über die Funktion "Dokumentenstatus bearbeiten"
					Die Seite zur Bearbeitung der Dokumente aufrufen. Hier können Sie die Dokumente abhaken die bereits eingegangen sind. Mit einem Klick auf
					"Auswahl als Vorliegend speichern" bestätigen sie den Eingang der ausgewählten Dokumente. Sollten sie versehentlich ein falsches Dokument
					markiert haben können sie den Haken jederzeit mit einem erneuten Klick entfernen und anschließend erneut speichern.
				</div>
				<h3 id="13">Wie schließe ich einen Bewerbungsvorgang ab?</h3>
				<div class="well">
					Hat ein Bewerber alle nötigen Dokumente eingereicht kann er einen Hiwi-Vertrag unterzeichen. Nach der Unterzeichnung können
					Sie den Bewerbungsvorgang aufrufen und auf "abschließen" klicken.
				</div>	
				<h3 id="14">Wie benutze ich die Vertreterfuntion?</h3>
				<div class="well">
					In ihrem Profil können Sie die Email-Adresse eines Vertreters angeben, dieser muss allerdings auch im System registriert sein.
					Sollten Sie einmal ihre Ausschreibungen nicht selbst bearbeiten können kann ihr Vetreter über diese Funktion auf ihre Nachrichten
					und Ausschreibungen zugreifen und bei Bedarf bearbeiten. Um in den Vertretermodus zu wechseln klicken sie oben rechts auf ihren Namen
					und in dem Menü auf Vertretermouds.
				</div>			
			</s:if>				
			</div>
		</div>
	</div>
</div>