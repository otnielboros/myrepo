# ##Yuhuu-backend install tutorial


## Authentication process:  
        * client: testjwtclientid  
        * secret: XY7kmzoNzl100  
        * Non-admin username and password: user | user  
        * Admin user: admin | admin

*Folosim postman pentru testare*

1. Generam un token de access a.k.a facem login:  
  
		  POST : localhost:8080/oauth/token  
		  Headers :   
		  Content-Type     application/x-www-form-urlencoded  
		  Authorization    Basic dGVzdGp3dGNsaWVudGlkOlhZN2ttem9OemwxMDA    //E acealasi lucru cu javascript : btoa(testjwtclientid)  
		    
		  Body :   
		  grant_type   password  
		  username     user  
		  password     user
  
	O sa primiti in body un JSON de forma

           {  
              "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidGVzdGp3dHJlc291cmNlaWQiXSwidXNlcl9uYW1lIjoiYWRtaW4uYWRtaW4iLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNDk0NDU0MjgyLCJhdXRob3JpdGllcyI6WyJTVEFOREFSRF9VU0VSIiwiQURNSU5fVVNFUiJdLCJqdGkiOiIwYmQ4ZTQ1MC03ZjVjLTQ5ZjMtOTFmMC01Nzc1YjdiY2MwMGYiLCJjbGllbnRfaWQiOiJ0ZXN0and0Y2xpZW50aWQifQ.rvEAa4dIz8hT8uxzfjkEJKG982Ree5PdUW17KtFyeec",  
              "token_type": "bearer",  
              "expires_in": 43199,  
              "scope": "read write",  
              "jti": "0bd8e450-7f5c-49f3-91f0-5775b7bcc00f"  
            }`
	  
Pentru a acessa datale datele trubie sa folositi ca si header in orice request acel token primit in forma  
  
		key=Authorization value=Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidGVzdGp3dHJlc291cmNlaWQiXSwidXNlcl9uYW1lIjoiYWRtaW4uYWRtaW4iLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNDk0NDU0MjgyLCJhdXRob3JpdGllcyI6WyJTVEFOREFSRF9VU0VSIiwiQURNSU5fVVNFUiJdLCJqdGkiOiIwYmQ4ZTQ1MC03ZjVjLTQ5ZjMtOTFmMC01Nzc1YjdiY2MwMGYiLCJjbGllbnRfaWQiOiJ0ZXN0and0Y2xpZW50aWQifQ.rvEAa4dIz8hT8uxzfjkEJKG982Ree5PdUW17KtFyeec
        
## Hai sa instalam proiectul

 - Java JDK 11        https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html  
 - verificati in Command Prompt cu javac -version  
 - daca nu merge comanda inseamna ca ati uitat sa setati variabila de environment (JAVA_HOME + MAVEN_HOME + M2_HOME)-> go check it  
   
   
 - Intellij IDEA Ultimate - posibil ca proiectul sa nu functioneze pe Comunity din moment ce nu ofera suport pentru spring.  
           - in caz ca nu il puteti lua, nu va merge aplicatia poate reusim sa va "abonez" la licenta mea, PM me  
  
 - MySQL with MySQL Workbench  
 - Accesati https://dev.mysql.com/downloads/file/?id=480824  
  
 - Cand apare Begin Your Download nu trebuie cont, e jos un link cu "No thanks, just start my download."  
  
 - MySQL Installer  
      - check I accept....  
      - select full  
      - check requirements trebuie sa aveti instalate toate chestiile ce vi le cere, intalati manual pe rand  
            ( posibil sa va ceara sa instalati python(nu stiu daca e doar la mine)  
      - next, next pana ajungeti la accounts and roles 
      - in prima jumatate setati parola de root ( feel free, dar sa nu o uitati, e important)  
      - next next next....  
 - Deschidem MySQL Workbench  
 - La MySQL Connections - Local instance MySqll.... selectati si va autentificati  
 - o sa va apara un query tab, daca nu file new query tab  
 - copiam acest cod magic si executam  
  

	     create database db_yuhuu; -- Create the new database create user 'springuser'@'localhost' identified by 'bubuesefu232'; -- Creates the user grant all on db_yuhuu.* to 'springuser'@'localhost'; -- Grant authority  
	      

 - and pretty much, that's all - daca nu vreti cu workbench, sper ca va descurcati, ala e scriptul...have fun  

 - Proiect  
   Folosim maven pentru dependente  
   Ca framework folosim Spring cu Java Based Annotations ( daca cineva vrea xml, fara suparare, sa se uite la calendar)  
      - daca nu intelegi annotarile nu e problema, va ajut si eu, daca nu, cautam impreuna  
   Pe langa spring pentru partea de Rest folosim Spring Boot  
   Ca ORM avem JPA with MySQL (mergem pe Code First)  
  
 - Packages  
    

	   configuration 
	      - sper sa nu fie nevoiti foarte multi dintre voi sa intre aici, ideea e 				  ca spring, si in special spring boot are nevoie    de cateva configurari  
	   model
	      - entitatile (user, companie, ....)  
          - pentru entitatile mari, sau alte clase folositi Builder Pattern, va rog! Aveti model pentru User;  
       repository - dupa cum vedeti folosim Spring JPA with MySQL, deci avem acces la  
           - JpaRepository care este o doar interfata, nu are nevoie de implementare si va explic de ce:  
           - puteti folosi custom query dar va recomand sa nu folositi, daca chiar va doriti...bafta;  
           - iti creaza un custom query in functie de numele si parametri metodelor ce le adaugati in interfata  
	                 Ex : pentru toti useri conti in username anumite litere adaugam in interfata metoda:  
                      
                        List<User> findAllByUserNameContaining(String letters);  
                 
		             La partea asta te ajuta mult intellij cu autocomplete (Alt + Space ;) )  
		             
	      - de asemenea iti realizeaza automat si paginare just search for it: Jpa Pagination , JpaRepository extinde PageRepository
              - plus multe altele, va rog cautati documentatia  
                 https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html
       service 
	       - toata logica o facem aici, TOATA, ABSOULUT TOATA, VA ROG
           - va rog frumos implementati aici tot ce e posibil, nu vreau logica in controller ce nu tine de trimiterea datelor  
           - sa incercam sa ne rezumam la un singur manager, daca nu va place, discutam argumentat  
       controller 
           - aici vom avea endpoints pentru toate entitatile, controller separat pentru fiecare entitate  
           - aici nu facem altceva inafara de trimitem date, primim date, sau trimitem exceptii la front-end in caz de nevoie    
       exception - va rog frumos, nu lasati exceptii netratate sau cu try catch ignored  
           - vom crea exceptii custom peste tot unde e nevoie in asa fel incat cei de pe front-end sa poata explica userului cat mai clar    unde e problema  
           - pe cat posibil sa tratam si situatii de nullpointer si chetii de genul  
       utilities - daca aveti nevoie de convertoare sau mai stiu eu ce minunati creati metode statice aici ca sa le poata folosi si alti 
          - VERIFICATI daca ceea ce aveti nevoie a mai fost creat, intrebati   
       validatoare - aici vom avea clasele ce se vor ocupa de validare  
            - cel putin aici, poate si in alte parti daca e nevoie vom folosi spring aspects (AOP)  
            - va rog sa nu imi sariti in cap la capitolul asta, o sa vedeti, nu sunt greu de folosit, just trust me please, daca nu va    place, promit sa nu insist

   Alte pachete sper sa nu apara, daca e nevoie discutam impreuna, va rog nu creati pachete de dragul de a crea      Unde se poate hai sa avem interfete      Ca si denumiri hai sa votam. Eu propun notarea de genul userId, userPasswor... .Decidem una impreuna si pe aia o folosim toti      Toate resursele gen imagini (poate va fii nevoie sa trimitem pe front), fisiere, etc, in resources, sper ca stie toata lumea      De asemenea in resources mai e si application.properties unde puteti adauga proprietati general valabile aplicatie, adaugati acolo sa le aiba toata lumea      O sa apra si un fisier ehcache.xml, il vom folosi pentru a cacheui data mari, alta chestie care o face Spring-ul dragut si usor, pacat sa nu o folosim      La teste, stiu ca o sa fiu injurat, dar am nevoie de cineva, cine doreste sa se ocupe de testare, nu vreau sa imi scrie teste, vreau doar sa ia fiecare entitate, metoda sa vada ca merge si trimite ceea ce se presupune ca trimite.

   Legat de Git, personal nu ma pricep foarte bine, dar pot incerca, in cazuri de urgenta sa ajut , kisses <3 
   
   Daca aveti probleme cu conectarea la baza de date, dupa ce a mers prima oara incercati sa dati drop la baza de date si la user si recreati
   La fel atunci cand modificati clase sau adaugati clase noi in model(entitati noi in baza de date) dati drop la toate tabele ( nu e neaparat necesar la toata baza de date)
   
    
    Pentru unele clase am construit buildere, va rog sa le folositi, gen pentru adresa
        Address address = new Address.Builder()
                                .withStreet(streetname)
                                .withNumber(number)
                             ..................................
                                .withPostalCode(postalCode)
                                .build();
   
    REGULI DE BUN SIMT, FOARTE FOARTE FOARTE IMPORTANT
    -Nu lasati System.out-uri cand dati push
    -Fara push fara sa porneasca aplicatia, atentie, nu rulati, vedeti ca merge, mai modificati o linie si dati push ca a mers acum o linie
    -Fara functii lungi, mai multe functii, mai putin cod, preferabil cat mai reutilizabil, please
    -Daca se poate extindem clase, interfete, abstract, plm
       P.S. NU extindeti clasele din model va rog, da eroare la crearea bazei de date 
    -Commit + push , nu pastrati cod pentru voi :))

