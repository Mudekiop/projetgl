<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<title>Page de connexion</title>
<link rel="stylesheet" type="text/css" href="./connexion.css">
<link
	href="https://fonts.googleapis.com/css?family=Montserrat:300,500,600"
	rel="stylesheet">
	
<style>
#error {
	color: #FF0000;
}
</style>
</head>
<body>
	<div id="arrierePlan">
		<div id="contenantDuLogin">
			<img id="logoEseo" src="./images/logo/logo_ESEO 1.svg" alt="LogoEseo">
			<form method="post" action="connexion">
				<div>
					<input id="email" type="text" name="email" placeholder="IDENTIFIANT" required="required" autocomplete="off" />
				</div>
				<div>
					<input id="mdp" type="password" name="mdp" placeholder="MOT DE PASSE" required="required" autocomplete="off"/>
				</div>
				
				<div>
					<input id="boutonSeConnecter" type="submit" value="SE CONNECTER" name="connect" onclick="return verifPublication()"/>
				</div>
				

			</form>
			<form method="post" action="home">
				<input id="boutonRetour" type="submit" value="RETOUR" />
			</form>
			<span id="motDePasseOublie">Mot de passe oublié ?</span>
			<p id="error">${errorMsg=="wrongPwd"?"Mauvais mot de passe":errorMsg=="notFound"?"Utilisateur inconnu":""}</p>

		</div>

	</div>

<script>

function verifPublication(){
	var email = document.getElementById("email").value;
	var mdp = document.getElementById("mdp").value;
	if(email.trim()==""){
		alert("Entrez un email valide");
		return false;
	}
	if(mdp.trim()==""){
		alert("Entrez un mot de passe valide");
		return false;
	}
</script>
</body>
</html>