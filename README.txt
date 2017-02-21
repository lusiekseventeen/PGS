<IDEA Project - kompilacja w IDE :( >


#PGS Zadanie rekrutacyjne#

Technologie:

Program zaimplementowany zgodnie ze wzorcem MVC (z racji intesywności pracy i braku czasu nie jest cudowny). Sam projekt został stworzony
dzięki InteliJ IDEA oraz SceneBuilder 

# Model - SQLite Database #
  DriverManager załatwia nam sprawę z używaniem bazydanych(trochę prymitywna) bez silnika.

# View - JavaFX #
  Front aplikacji mamy dzięki JavaFX. Główny szablon jest zaprojektowany w języku fxml (SceneBuilder), a dodatkowe komponenty
  są dynamicznie budowane już w samej javie.
  
# Controller - Java8 #
  W projekcie użyte są dogodności jakie oferuję nam najnowsza wersja javy czyli:
  -lamdy (głównie listenery)
  -nowe daty
  -strumienie (zaznaczanie wolnych dat w DatePicker'ach)
  
  
Sposób i opis użycia:

Do nawigacji i odświeżania widoków służy nam lewe menu;

Widok RENT:
  tutaj można wyszukać auto po rejestracji. Mamy dwa DatePickery, które dla danego auta dostosowują dostępność dat przy wyborze.

Widok RETURN:
  Można zwrócić auto po wpisaniu rejestracji.
  
Widok  LIST:
  Dostępnośc aut w przedziałach czasowy. Z tego poziomu można także wypożyczyć auto (przycisk na karcie samochodu, który 
  przenosi nas do widoku RENT).

Widok HISOTRY:
  Hisotria cała nie reagująca na usuwanie aut.
  
Widok CARS:
  Dodawanie, modyfikacja i usuwanie aut z floty.
  
Znane bugi i ulepszenia:
  Z racji braku czasu wiem o błędy i ulepszeniach, których nie zdążyłem zaimplementować:
  
  + brak filtrowania inputu użytkownika (głównie długość)
  + listener on resize (niektóre widoki powinny zmieniać wrapowanie po resizie, żeby karty samochodów się układały na całej długości)
  + po wybraniu wolnego auta w widoku i przeskoczeniu do widoku rent wartości z datepickerów także powinny się przenieść (szybkość działania)
  + animacje... i hovery w menu
  
