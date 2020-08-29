# Hacktyki 2020
Z powodu panującej pandemii COVID-19 realizacja praktyk studenckich w roku 2020 okazała się być znacznie utrudniona niż w ubiegłych latach. Firma EUVIC postanowiła wyść naprzeciw potrzebom studentów i wymyśliła alternetywne rozwiązanie.
Hacktyki są połączeniem standardowych praktyk studenckich i hackatonu. Każdy uczestnik miał do wyboru jeden z wielu tematów który będzie realizował w wybranej technologii i co najważniejsze - bezpiecznie z domu. Nad postępami pracy czuwali mentorzy, którzy także służyli chętnie pomocą w razie problemów napotkanych w trakcie pisania kodu.

# Temat projektu
Temat projektu, który realizowałam podczas hacktyk to system umożliwiający rezerwację kortów do squasha. Polecenie mówiło o właścicielu posiadającym 3 kluby z kortami dla których rezerwacje dotychczas odbywają się w sposób telefoniczny. 
Realizowany przeze mnie system miał być narzędziem do zautomatyzowania procesu rezerwacji. Aby uzyskać zaliczenie praktyk od mentora, należało utworzyć backendową część aplikacji w postaci REST API.

# Założenia 
System został przeze mnie zaprojektowany w następujący sposób:
- Dodanie rezerwacji wymaga od użytkownika rejestracji swojego konta
- Rejestracja autoryzowana była poprzez JWT
- W systemie istniały role użytkowników blokujące niektóre możliwości aplikacji dla nieupoważnionych do nich osób
- W ramach zabezpieczenia przed rezerwacją nadmiernej ilości terminów, na których osoba nie ma zamiaru się pojawić wprowadzone zostało ograniczenie do dwóch aktywnych rezerwacji na konto
- Rezerwacje w bazie danych dezaktywują się po upłynięciu czasu ich zakończenia
- Korty można dezaktywować, a operacja ta nie zezwala na dodawanie nowych rezerwacji dla nieaktywnego kortu (bez jednoczesnego usuwania aktualnie istniejących już rezerwacji)
- W momencie usunięcia Użytkownika automatycznie usunięte zostają jego rezerwacje 
- W przypadku usunięcia całego klubu, z bazy danych usuwają się przypisane do niego korty i rezerwacje.
- Kluby i korty mogą być dodawane, usuwane bądź modyfikowane dowolnie przez użytkowinka o odpowiedniej roli.
