# TP_Othello

##Auteurs : 
    Lukas Bitter
    Margaux Divernois

##Cours:
    HE-Arc DLM
    Cours d'Intelligence Artificielle
    Année 2015-2016
    
##Explications de la fonction d'évalutation:
Tout d'abord, notre fonction d'évaluation utilise 4 éléments : 
    - Matrice d'évaluation donnant une valeur à chaque position
    - Mobilité (Différence entre "nos" déplacements possibles et les déplacements possibles de l'adversaire)
    - Matériel (Différence entre "nos" pièces sur le jeu et celles de l'adversaire)
    - Coins (Différence entre le nombre de coins possédés par "nous" et l'adversaire)

Ces 4 éléments sont multipliés par des coefficiants, puis additionnés entre eux afin de donner l'évaluation du mouvement : 
    
    A * matrice + B * mobilité + C * matériel + D * coins 

Nous avons également choisi d'adapter le coefficients en fonction du moment de la partie : 
    Etape d'ouverture (Moins de 12 pièces posées)
    Cours de partie
    Fin de partie

##Sources
Voici quelques sources que nous avons utilisé à titre informatif:
    http://www.ffothello.org/othello/principes-strategiques/
    http://www.ffothello.org/informatique/algorithmes/
    http://imagine.enpc.fr/~monasse/Info/Projets/2003/othello.pdf
    http://www3.ntu.edu.sg/home/ehchua/programming/java/javagame_tictactoe_ai.html
    http://www.radagast.se/othello/howto.html
    https://kartikkukreja.wordpress.com/2013/03/30/heuristic-function-for-reversiothello/
    