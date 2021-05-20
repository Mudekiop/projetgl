<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<title>Publier Audit</title>
<%@include file="bibliotheques.jsp"%>
<link rel="stylesheet" href="style.css">
<link rel="preconnect" href="https://fonts.gstatic.com">
<link href="https://fonts.googleapis.com/css2?family=Mulish:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
</head>
<body>
	<%@include file="menu.jsp"%>	
	<div id="containerVuePrincipale">
		<span class="titre">${ modele.getTitre() }</span>
		<div id="contenuPrincipale">
			<div>
				<div class="titre">
					Listes des questions de l'audit :
				</div>
				<c:forEach items="${ modele.getListeSection() }" var="section">
					<c:forEach items="${ section.getListeQuestion() }" var="question">
						<p>
							<c:out value="${ question.getContenu() }" />						
						</p>					
					</c:forEach>
				</c:forEach>
			</div>
		
			<form method="post" action="publierAudit">
				<input hidden="hidden" value="${ modele.getId() }" name="idModele" id="idModele">
				<div>
					<label for="titre">Titre de l'audit</label>
					<input type="text" name="titre" id="titre" required="required"/>
				</div>
		
				<!-- Jquery -->
				<div>
					<label for="dateOuverture">Date d'ouverture de l'audit</label>
					<input type="text" name="dateOuverture" id="dateOuverture" required="required" autocomplete="off"/>
				</div>
		
				<div>
					<label for="dateFermeture">Date de fermeture de l'audit</label>
					<input type="text" name="dateFermeture" id="dateFermeture" required="required" autocomplete="off"/>
				</div>
				
				<div>
					Cet audit concerne :
					<div>
						Responsables d'option :
						<select style="width: 200px;" multiple="multiple" name="listeRespOption" id="listeRespOption">
							<c:forEach items="${ listeResponsableOption }" var="respOption">
								<option value="${ respOption.getId() }"><c:out value="${ respOption.getPrenom() }" /> <c:out value="${ respOption.getNom() }" /></option>
							</c:forEach>
						</select>
					</div>
					<div>
						Responsables d'UE :
						<select style="width: 200px;" multiple="multiple" name="listeRespUE" id="listeRespUE">
							<c:forEach items="${ listeResponsableUE }" var="respUE">
								<c:if test="${ sessionScope.utilisateur.getId()!=respUE.getId() }">
									<option value="${ respUE.getId() }"><c:out value="${ respUE.getPrenom() }" /> <c:out value="${ respUE.getNom() }" /></option>								
								</c:if>
							</c:forEach>
						</select>
					</div>
					<div>
						Encadrant matière :
						<select style="width: 200px;" multiple="multiple" name="listeEncadrantMatiere" id="listeEncadrantMatiere">
							<c:forEach items="${ listeEncadrantMatiere }" var="encadrantMatiere">
								<option value="${ encadrantMatiere.getId() }"><c:out value="${ encadrantMatiere.getPrenom() }" /> <c:out value="${ encadrantMatiere.getNom() }" /></option>
							</c:forEach>
						</select>
					</div>
					<div>
						Eleves :
						<select style="width: 200px;" multiple="multiple" name="listeEleve" id="listeEleve">
							<c:forEach items="${ listeEleve }" var="eleve">
								<option value="${ eleve.getId() }"><c:out value="${ eleve.getPrenom() }" /> <c:out value="${ eleve.getNom() }" /></option>
							</c:forEach>
						</select>
					</div>
					<div>
						L'audit concerne l'UE :
						<select style="width: 200px;" id="ue" name="ue">
							<option value="0">Choisissez une UE</option>
							<c:forEach items="${ listeUe }" var="Ue">
								<option value="${ Ue.getId() }"><c:out value="${ Ue.getTitre() }" /></option>
							</c:forEach>
						</select>
					</div>
				</div>
				
				<div>
					<input type="submit" name="btn_publierAudit" value="Publier l'audit" onclick="return verifPublication()"/>		
				</div>
		
			</form>
		</div>			
	</div>
	

<script type="text/javascript">
	$.datepicker.setDefaults({
		altField : "#datepicker",
		closeText : 'Fermer',
		prevText : 'Précédent',
		nextText : 'Suivant',
		currentText : 'Aujourd\'hui',
		monthNames : [ 'Janvier', 'Février', 'Mars', 'Avril', 'Mai', 'Juin',
				'Juillet', 'Août', 'Septembre', 'Octobre', 'Novembre',
				'Décembre' ],
		monthNamesShort : [ 'Janv.', 'Févr.', 'Mars', 'Avril', 'Mai', 'Juin',
				'Juil.', 'Août', 'Sept.', 'Oct.', 'Nov.', 'Déc.' ],
		dayNames : [ 'Dimanche', 'Lundi', 'Mardi', 'Mercredi', 'Jeudi',
				'Vendredi', 'Samedi' ],
		dayNamesShort : [ 'Dim.', 'Lun.', 'Mar.', 'Mer.', 'Jeu.', 'Ven.',
				'Sam.' ],
		dayNamesMin : [ 'D', 'L', 'M', 'M', 'J', 'V', 'S' ],
		weekHeader : 'Sem.',
		dateFormat : 'yy-mm-dd'
	});

	$(function() {
		$("#dateOuverture").datepicker({
			minDate:0
		});
	});
	
	$(function() {
		$("#dateFermeture").datepicker({
			minDate:0
		});
	});
	
	$('#dateOuverture').change(function() {
		var datestart=$("#dateOuverture").datepicker("getDate");
		$("#dateFermeture").datepicker('option', 'minDate', datestart);
	})
	
	$('#dateFermeture').change(function() {
		var datestart=$("#dateFermeture").datepicker("getDate");
		$("#dateOuverture").datepicker('option', 'maxDate', datestart);
	})
	
	$("#dateOuverture").on('keydown paste focus', function(e){
        if(e.keyCode != 9) // ignore tab
            e.preventDefault();
    });
	
	$("#dateFermeture").on('keydown paste focus', function(e){
        if(e.keyCode != 9) // ignore tab
            e.preventDefault();
    });

	function verifPublication(){
		var titre = document.getElementById("titre").value;
		if(titre.trim()==""){
			alert("Entrez un titre valide");
			return false;
		}
		var ue = document.getElementById("ue").value;
		if(ue=="0"){
			alert("Choisissez une UE");
			return false;
		}
		var listeRespOption = document.getElementById("listeRespOption").value;
		var listeRespUE = document.getElementById("listeRespUE").value;
		var listeEncadrantMatiere = document.getElementById("listeEncadrantMatiere").value;
		var listeEleve = document.getElementById("listeEleve").value;
		if(listeRespOption=="" && listeRespUE=="" && listeEncadrantMatiere=="" && listeEleve==""){
			alert("L'audit doit concerner un ou plusieurs utilisateurs");
			return false;
		}
	}
	
</script>

</body>
</html>