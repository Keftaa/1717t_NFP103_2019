# Talk IRC "WHATSUP lite" (1698T_NFP103_2019) - Adnan Hamzeh

**Ceci est une explication rapide de ma propre implémentation. Pour les données, voir README.**  



## A. Comment utiliser le serveur
0. Cloner le projet et naviguer dans un Terminal vers `out\production\ServerTCP\app`  
1. Il suffit de démarrer le serveur (`java app.ServerTCP`) qui écoutera sur le port 2000.  
2. Les commandes \_kill et \_shutdown peuvent provenir du clavier.  
3. Les commandes \_who, \_connect et \_quit peuvent provenir des clients.

## B. Comment utiliser le client
0. Naviguer dans un Terminal vers `out\production\ClientTCP\app`  
1. Après avoir démarré le client sans arguments (`java app.ClientTCP`), il suffit d'appeler la commande \_fetch qui retournera une liste de serveurs disponibles.
2. Ensuite, tapez la commande \_help pour savoir tout le reste !

**Des problèmes  ? N'hésitez pas d'ouvrir un "issue" !**
