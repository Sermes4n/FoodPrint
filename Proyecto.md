FITXA DE PROJECTE: "EcoScanner" 


Institució: Escola Pia Nostra Senyora 


Codi: 


Revisió: 00 

1. Justificació i contextualització
El transport de mercaderies, especialment en el sector de l'alimentació, és una de les principals fonts d'emissions de gasos d'efecte hivernacle. Com a futurs desenvolupadors (DAM), el vostre repte és crear el Producte Mínim Viable (MVP) d'una app mòbil a Android (amb Jetpack Compose) que fomenti el consum de proximitat (Km 0).
+1

L'aplicació permetrà a l'usuari:

Escanejar un producte.

Identificar-ne el país o lloc d'origen.

Calcular la distància fins a la posició actual de l'usuari.

Informar de la contaminació (emissions de CO2) causada pel transport.

Requerir un inici de sessió (Login) per portar un registre segur de les emissions estalviades.

2. Resultats d'aprenentatge (RA)
Aquest projecte avalua de manera integrada tres mòduls professionals:


MP0489 (Programació multimèdia): Aplica tecnologies de desenvolupament, programes que integren continguts multimèdia i maquinari com la càmera i el GPS.


MP1665 (Digitalització): Caracteritza les tecnologies habilitadores digitals, avalua la importància i protecció de les dades (ciberseguretat) i l'ús d'APIs i Cloud.


MP1708 (Sostenibilitat): Proposa productes responsables basats en l'economia circular i elabora informes de sostenibilitat amb mètriques de càlcul de CO2.

3. Requisits tècnics i funcionals
L'aplicació i la documentació han de complir els següents requisits per ser avaluats com a MVP:


Desenvolupament de la Interfície (UI): Interfície nativa a Android Studio amb Jetpack Compose, incloent-hi obligatòriament una pantalla de Login.


Maquinari: Ús de la càmera per escanejar (codi de barres o simulació) i sensor GPS per obtenir la ubicació actual.


Dades i APIs: Connexió via HTTP a una API (real com Open Food Facts o mockejada) per l'origen i càlcul de distància/CO2.


Dossier digital: Informe sobre arquitectura Cloud, ciberseguretat de l'historial i integració de Big Data/IA.


Dossier sostenible: Informe sobre factors de conversió de CO2, impacte ambiental i promoció dels ODS.

4. Esquema i Flux de l'Aplicació
L'app es divideix en quatre grans blocs lògics :


LOGIN: Autenticació d'usuaris.


ESCANER/HOME: Ús de multimèdia i GPS.


DADES/CÀLCUL: Lògica de càlcul de CO2.


STATS/IMPACT: Visualització de sostenibilitat i IA.

Elements de la Interfície (Mockup)

Pantalla de Login: Correu electrònic, contrasenya, botó "Inicia sessió", "Registra't" i recuperació de contrasenya .


Pantalla Principal: Cercador de productes, ubicació actual, maquinari actiu i botó "Escanejar producte" .

Detall del Producte (Exemple: Iogurt de Maduixa):

Origen: Nova Zelanda (Auckland).
+1

Càlcul de contaminació: 32,000 Km de distància estimada.
+1

Impacte: 75% Productes Km 0 vs 25% Altres.

Estalvi: 450 Kg de CO2 estalviat fins ara i 12,500 L d'aigua.
+1

Opcions: Triar alternativa Km 0 o proposar millora.

5. Organització, eines i logística

Equips: 2-3 persones amb divisió de rols (UI/Sostenibilitat vs Backend/Digitalització), tot i que tothom ha de programar.

Logística d'entrega: Lliuraments setmanals al Classroom (divendres 23:55). El codi només s'avalua via repositori Git.

Caixa d'eines:


APIs: Open Food Facts i Climatiq.


Disseny: Figma i Canva.


Versions: GitHub o GitLab.

6. Fases del projecte (Sprints)
Sprint 1: Fórmules de CO2, dades i privacitat (Setmana 1) 


Investigació: Definir la fórmula matemàtica i els factors de conversió de CO2 per tona/quilòmetre segons el transport (avió, vaixell, camió).


Arquitectura de dades: Disseny de l'esquema JSON (nom_producte, pais_origen, latitud_origen, longitud_origen).


Ciberseguretat: Estructura del Login i política de privacitat per a l'historial GPS.


Lliurable: PDF amb fórmules, esquema JSON i proposta de privacitat.

Sprint 2: Prototipatge i UI en Compose (Setmana 2) 


Configuració: Creació del projecte a Android Studio.


Pantalles: Login, Pantalla Principal (escaner) i Pantalla de resultats (gràfica atractiva amb equivalències com icones de cotxes o arbres) .


Lliurable: Enllaç al repositori amb navegació funcional entre les 3 pantalles.

Sprint 3: Maquinari i connexions HTTP (Setmana 3) 


Sensors: Permisos a l'Android Manifest, coordenades GPS i obertura de càmera.


Connexió: Petició GET a l'API amb Retrofit o Ktor.


Lògica: Càlcul de la distància real entre l'usuari i l'origen, aplicant la fórmula de CO2.


Lliurable: App capaç d'agafar la ubicació, llegir l'API i mostrar el càlcul a la pantalla.

Sprint 4: IA, empaquetament i pitch (Setmana 4) 


Empaquetament: Generació de l'executable APK signat.


Memòria final: Explicació de l'evolució amb Big Data (optimització de rutes) i IA (reconeixement de text en etiquetes).


Pitch: Preparació d'una presentació de 5-10 minuts.


Lliurable: Arxiu APK, memòria final i presentació oral.

7. Estructura de la Memòria Final
Document PDF de 5-10 pàgines amb els següents apartats obligatoris:


Introducció i propòsit sostenible: Problema ambiental del transport, beneficis del Km 0 i ODS 12 i 13 .
+1


Funcionament tècnic: Documentació de la interfície Compose, sensors i detall de la fórmula matemàtica .
+1


Cicle de la dada i seguretat: Cicle de vida de la dada (de la foto al resultat), infraestructura Cloud i política de privacitat/seguretat .
+1


Evolució (IA i Big Data): Ús de la IA per llegir etiquetes i Big Data per anàlisi logístic.
+1


Mètriques d'impacte: Indicadors ASG (kg de CO2 estalviats) i conclusions .

8. Guia de la Presentació Oral (Pitch)

Durada: 5-10 minuts.


El problema (1 min): Dada impactant sobre la petjada de transport.
+1


La solució (5 min): Demostració de la UI en Compose, escaneig, GPS i resultats de contaminació amb equivalències .
+1


Tecnologia i dades (2 min): Cicle de la dada, Cloud i privacitat .


El futur (1 min): Ús de la IA per a productes sense codi de barres i tancament amb la mètrica d'impacte .

9. Rúbrica d'avaluació
Criteri d'avaluació	Excel·lent (9-10)	Bé (7-8)	Bàsic (5-6)
Rigor dels càlculs de CO2	
Fonts contrastades, factors justificats detalladament, error < 10%. Equivalències creatives. 
+1

Fonts adequades, errors < 20% en la matemàtica del CO2. Equivalències funcionals. 
+1

Dades aproximades o inventades sense justificació. Equivalències simples o absents. 
+1

Integració d'APIs i dades	
Integració impecable amb APIs reals, descàrrega asíncrona correcta i gestió completa d'errors. 
+1

API real o Mockejada ben estructurada. Gestió bàsica d'errors. 
+1

API amb problemes o ús de dades locals estàtiques sense format JSON clar. 
+1

GPS i càmera funcionals	
GPS ràpid i escàner de càmera en temps real. 
+1

GPS funcional però lent. Escàner mitjançant foto i acceptació. 
+1

Un sol sensor funcional o integració amb problemes importants. 
+1

Anàlisi de digitalització	
Anàlisi profunda de Cloud, privacitat del Login i com el Big Data/IA pot predir rutes. 
+1

Pla teòric d'IA i seguretat. 

Connexions superficials. Poc aprofundiment en el tractament de la dada i ciberseguretat. 
+1

Pla de sostenibilitat	
ODS identificats. Informe ASG focalitzat en food miles. Impacte quantificat. 

ODS identificats. Informe correcte. Es parla de transport però menys profundament. 

ODS mencionats superficialment. Sense mètriques clares ni connexió forta amb l'estalvi. 

UX i Engagement (UI Compose)	
Pantalles intuïtives, equivalències sorprenents i disseny amb gamificació que enganxa. 

App neta i usable. El flux (Login -> Escaner -> Resultat) és correcte. 

App funcional però estèticament pobra o amb problemes de navegació.