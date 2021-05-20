<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="style.css">
<link rel="preconnect" href="https://fonts.gstatic.com">
<link
	href="https://fonts.googleapis.com/css2?family=Mulish:wght@300;400;500;600;700;800&display=swap"
	rel="stylesheet">
<title>Modification Audit</title>
</head>
<body>
<%@include file="menu.jsp"%>
	<div id="containerVuePrincipale">
		<span class="titre">Audits</span>
		<div id="contenuPrincipale">
			<div id=titreParamètres>
				<div id=titre>
					<span class="titre">Résultats en cours de l'audit :
						${audit.getTitre()}</span>
				</div>
			</div>
			<form method="post" action="modificationAudit?id=${ audit.id }">
				<div id=tableau>
					<p>
	  					<c:forEach var="reponses" items="${reponses}" varStatus="vs">
							<c:out value="${vs.count})"/>
	      					<c:out value="${reponses.getQuestion().getContenu()}"/>
	      					<br />
	      					<c:choose>
	      						<c:when test="${empty reponses.contenuReponse}">
	      							<c:out value="Cette question n'a pas de réponse associée"/>
	      						</c:when>
	      						<c:otherwise>
	      							<input type="hidden" id="reponseID" name="reponseID${vs.count}" value="${reponses.id}">
	      							<input type="text" id="reponseModifiee" name="reponseModifiee${vs.count}" value="${reponses.contenuReponse}"/>
	      						</c:otherwise>
	      					</c:choose>
	      					<br />
	      					<br />
	  					</c:forEach>
	  					<input id="boutonEnregisterModif" type="submit" value="Enregistrer les modifications" name="enregistrerModif"/>
					</p>
				</div>
			</form>
		</div>
	</div>
</body>
</html>