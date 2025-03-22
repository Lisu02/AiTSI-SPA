# **AiTSI - SPA**


## **Podstawowe informacje**



## **Frontend**



## **Query**



## **PKB**

### Struktura

- **API**: Interfejs publiczny dla innych modułów w projekcie
- **Source**: Kod źródłowy wewnętrzny dla grupy piszącej PKB. Ma być pisany z założeniem, że jak coś tam jest źle, to winna jest grupa PKB (chyba, że inna grupa nie potrafi sprecyzować co chce, ale zakładam, że nie)
- **Old**: Rzeczy do przepisania (w przyszłości jak najszybszej też do wyrzucenia)

### API - Specyfikacja

- **Enumy**:
    - **EntityType**: Rodzaje wierzchołków tworzonych na bazie SIMPLE
    - **LinkType**: Łączenia wierzchołków
- **PKB**: Klasa trzymająca baze
- **Interfejsy**:
  - **IVarTable**: Tablica zmiennych
  - **IAST**: Specyfikacja API drzewa AST
  - **ICalls, IModifies**: Specyfikacja Struktur Call/Madify (wedle Miro to część Design Extractora, także do przedyskutowania z Frontem, czy to zostaje).
- **Inne**:
  - **TNode**: Interfejs wierzchołka z drzewa AST (specjalnie pusty, nie obchodzi nikogo co tam jest, po to mamy API, jak będzie potrzeba to dodamy, ale do przedyskutowania)
  - **Attr**: Opakowka Attrybutów (Zmianna/Stała, prośba o sprecyzowanie/implementacje Frontu jak oni by widzieli obsługę zmiennych, bo my tylko wstawiamy to do drzewa na 99%).

### Wyjątki

- **ASTBuildException**: Gdy walnie przy budowie drzewa. Nie wiem czy koniec końców potrzebne, ale te łączenie jest void, a tam może walnąć, że nie ma miejsca już dla nowego wierzchołka, moim zdaniem cała budowa do przegadania, chyba, że front już wie jak buduje to spoko.
- **NotImplementedRuntimeException**: Jak coś niezaimplementowane, to wywali. Jest to RuntimeException, więc nie informuje tego definicja.

### Source - Info

1. Piszemy używając tak jak można Dependency injection (mówcie jak coś poprawić bo tego nie ma a się da): https://www.freecodecamp.org/news/a-quick-intro-to-dependency-injection-what-it-is-and-when-to-use-it-7578c84fa88f/
2. Budowanie hierarchii klas wierzchołków drzewa:
   1. Interfejs TNode jest wyjściowy i wszystko ma po nim dziedziczyć
   2. Za nim powinien pojawić się abstract spełniający podstawowe funkcjonalności, oraz definiujący wszystko, co potrzebne z zewnątrz dla nas jako PKB.
   3. Następnie wychodzą rozgałęzienia dla specyficznych typów wierzchołków na bazie Entity trzymanego (StmtList, While, Assign, itd.) Tu czy coś ma być pomiędzy to kwestia własna, byle by miało ręce i nogi.
   4. Tworzeniem konkretnej instancji zajmie się IAST (przynajmniej wstępnie)
3. Tworzenie AST
   1. Dwa rozwiązania do obgadania:
      1. Jedna wielka instancja (proste w implementacji ale bałagan)
      2. Hierarchia wielu klas implementujących odpowiednie funkcjonalności (trudniejsze, ale łatwiej trzymać porządek tego co się dzieje)
4. Na ile można trzymać zasady SOLID: https://www.baeldung.com/solid-principles
5. Tworzenie nowych pakietów wewnątrz Source jak najbardziej OK o ile to ma sens, a nawet wskazane jeżeli ma zachować porządek!!!
6. To co jest w OLD ma sens, ale nie używać bezpośrednio tego, tylko próbować to przepisać na nową hierarchie.