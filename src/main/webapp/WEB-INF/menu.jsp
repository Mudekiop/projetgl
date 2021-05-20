<div id="containerPrincipale">
		<div id="containerMenuDeNavigation">
			<nav class="menuDeNavigation">
				<span role="link" class="profil"> 
				<img src="images/icones/photoProfil.png" alt="PhotoProfil"> <span>${sessionScope.utilisateur.getPrenom()} ${ sessionScope.utilisateur.getNom() }</span>
				</span>
				<div class="groupe1">
					<div class="objetNavigation">
						<span role='link' class="lienObjetNavigation"> <img
							src="images/icones/accueil.png" class="icone" alt="Accueil">
							<span>Accueil</span>
						</span>
					</div>
					<c:if test="${ sessionScope.utilisateur.isResponsableOption() || sessionScope.utilisateur.isResponsableUe() || sessionScope.utilisateur.isEncadrantMatiere() || sessionScope.utilisateur.isAdministrateur()}">
						<div class="objetNavigation">
							<span role='link' class="lienObjetNavigation"> <img
								src="images/icones/audit.png" class="icone" alt="Mes audits">
								<span><a href="audits">Mes audits</a></span>
							</span>
						</div>					
					</c:if>
					<div class="objetNavigation">
						<span role='link' class="lienObjetNavigation"> <img
							src="images/icones/resultat.png" class="icone" alt="Résultats">
							<span><a href="accueilResultat">Résultats</a></span>
						</span>
					</div>
					<c:if test="${ sessionScope.utilisateur.isAdministrateur() }">
						<div class="objetNavigation">
							<span role='link' class="lienObjetNavigation"> <img
								src="images/icones/parametre.png" class="icone" alt="Gestion des utilisateurs">
								<span><a href="accueilAdmin">Gestion des utilisateurs</a></span>
							</span>
						</div>
					</c:if>
					<c:if test="${ sessionScope.utilisateur.isResponsableUe()}">
						<div class="objetNavigation">
							<span role='link' class="lienObjetNavigation"> <img
								src="images/icones/parametre.png" class="icone" alt="Publier un audit">
								<span><a href="publierAudit">Publier un audit</a></span>
							</span>
						</div>
					</c:if>
				</div>
				<div class="groupe2">
					<div class="objetNavigation">
						<span role='link' class="lienObjetNavigation"> <img
							src="images/icones/profil.png" class="icone" alt="Mon profil">
							<span><a href="monProfil">Mon profil</a></span>
						</span>
					</div>
					<div class="objetNavigation">
						<!-- <span role='link' class="lienObjetNavigation"> <img
							src="images/icones/deconnexion.png" class="icone"
							alt="Déconnexion"> <span>Déconnexion</span>
						</span>-->
						<form method="post" action="connexion">
							<input type="submit" name="deco" value="Déconnexion"/>
						</form>
					</div>
				</div>
			</nav>
		</div>
	</div>