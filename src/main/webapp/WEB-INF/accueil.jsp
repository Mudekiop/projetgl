<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<link rel="stylesheet" href="style.css">
<title>Page d'accueil</title>
</head>
<body>
	<h1>Page d'accueil</h1>

	<a href="audits">Mes audits</a>
	
	
	<!--  FORMULAIRE DE CONNEXION -->
	<c:choose>
		<c:when test="${ empty sessionScope.utilisateur.getEmail()}">
			<a href="connexion"><input type="button" value="Connexion"></a>	
		</c:when>
		
		<c:when test="${ !empty sessionScope.utilisateur.getEmail()}">
			<p>Connecté en tant que ${sessionScope.utilisateur.getPrenom()} ${ sessionScope.utilisateur.getNom() } !</p>
			
			<form method="post" action="connexion">
				<input type="submit" value="Deconnexion" name="deco" id="deco"/>
			</form>
		</c:when>
	</c:choose>
</body>
</html>