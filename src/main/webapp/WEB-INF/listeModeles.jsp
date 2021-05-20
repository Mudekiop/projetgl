<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Liste des modeles</title>
<link rel="stylesheet" href="style.css">
<link rel="preconnect" href="https://fonts.gstatic.com">
<link href="https://fonts.googleapis.com/css2?family=Mulish:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
<%@include file="bibliotheques.jsp"%>
</head>
<body>
	<%@include file="menu.jsp"%>
	<div id="containerVuePrincipale">
		<span class="titre">Liste des modèles</span>
			<div id="contenuPrincipale">
				<table>
					<c:forEach items="${ listeModeles }" var="modele">
						<tr title="${ modele.getListeQuestionString() }">
							<td>
								<a href="<%=application.getContextPath()%>/publierAudit?id=${ modele.getId() }">
									<c:out value="${ modele.getTitre() }"></c:out>					
								</a>
							</td>
						</tr>
					</c:forEach>	
				</table>			
			</div>
	</div>


</body>
</html>