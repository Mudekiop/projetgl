<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="style.css">
<link rel="preconnect" href="https://fonts.gstatic.com">
<link
	href="https://fonts.googleapis.com/css2?family=Mulish:wght@300;400;500;600;700;800&display=swap"
	rel="stylesheet">
<title>Mon Profil</title>
</head>
<body>
	<%@include file="menu.jsp"%>
	<div id="containerVuePrincipale">
		<span class="titre">Mon profil</span>
		<div id="contenuPrincipale">
			<div id=titreParam�tres>
				<div id=titre>
					<span class="titre">Informations d�taill�es</span>
				</div>
			</div>
			<hr>
			<section id="monProfil">
				<c:if test="${ !empty sessionScope.utilisateur.getEmail() }">
					<div>
						<div class="left">
							<p>Pr�nom :</p>
							<p>Nom :</p>
							<p>Adresse mail :</p>
							<p>R�le :</p>
							<p>Avatar :</p>
						</div>
						<div class="right">
							<p>${sessionScope.utilisateur.getPrenom()}</p>
							<p>${sessionScope.utilisateur.getNom()}</p>
							<p>${sessionScope.utilisateur.getEmail()}</p>
							<p>${sessionScope.utilisateur.getListeRoles()}</p>
							<img src="images/icones/photoProfil.png" alt="PhotoProfil">
						</div>
					</div>
				</c:if>
				<c:if test="${ empty sessionScope.utilisateur.getEmail() }">
					<p>Vous n'avez pas d'audits</p>
				</c:if>
			</section>

			<form method="post" action="home">
				<input id="boutonRetour" type="submit" value="RETOUR" />
			</form>
		</div>
	</div>

</body>
</html>