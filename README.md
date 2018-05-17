# Assignemnt03PCD

1) Game of Life - Actor-based


Implementare in Java o in Scala una versione ad attori del gioco “The Game of Life” - già incontrato nell’assignment #1. Considerare i medesimi requisiti specificati per l’assignment #1 (https://goo.gl/11Z1zY). In questo caso la soluzione deve essere basata completamente sul modello ad attori (usando Akka come tecnologia di riferimento), eventualmente integrandolo con parti non ad attori quando necessario (es: uso librerie per la GUI). Valutare le performance del sistema, includendo le relative considerazioni sul report.


NOTA:

è possibile riusare le parti già sviluppate con l’assignment #1, incluse le soluzioni presentate a lezione.


2)  Actor-based distributed sensors network


Un sistema di attori è utilizzato per modellare (simulare) un insieme N di sensori, distribuiti su un certo territorio, usando 1 attore per ogni sensore.  Si presuppone che ogni attore/sensore misuri periodicamente - con frequenza che varia da sensore a sensore - una certa grandezza v, rappresentata da un valore intero compreso fra 0 e 100. Mediante una GUI, si deve dare la possibilità ad un utente di:


determinare e visualizzare una global snapshot consistente dell’insieme dei sensori (pulsante snap!), usando l’algoritmo di Chandy-Lamport (evitando quindi di bloccare e sincronizzare i vari attori)

Simulare la failure di un sensore (pulsante fail), imponendo la terminazione forzata di un sensore/attore scelto in modo casuale.




La consegna consiste in una cartella “Assignment-03” compressa (formato zip) contenente

cartella src con i sorgenti del programma
cartella doc con la relazione in PDF (report.pdf)
