The program should model a knight in his shiny armor moving on a
board filled with obstacles.
Following his king’s commands,
theknight should move on theboard,following some simple
rules and at the end of the excution the ouput should represent his position
and direction.

//---------------- RAGIONAMENTO ------------------------------------//

Il concetto di funzionamento è stato quello di dare al cavaliere una cordinata x e y
e alla board una sua dimensione e ostacoli.

Supponendo che quando la direzone è NORD aumenta y mentre quando è EST aumenta X
(viceversa con sud ed ovest) abbiamo ciclato punto per punto la nuova x,y del cavaliere
e abbiamo verificato che la nuova posizione xy non fosse OUT_OF_BOARD-->errore
oppure non fosse su un ostacolo (in quel caso c'è il breack del ciclo e si passa al 
comando successivo come da traccia)

Abbiamo aggiunto il concetto di Context per non perdere le info e lo status del cavaliere
durante i movimenti.

Abbiamo aggiunto un parser per recuperare il json e separare il tipo di comando
start move rotate dalle cordinate x e y.


