// Bartosz Bugajski - 5
import java.util.Scanner;

/*
Zestawy danych wczytuje do tablicy dwuwymiarowej, w ktorej nastepnie zamieniam miejscami wiersze, aby posortowac
ja wedlug danej kolumny. Dla ilosci danych do posortowania mniejszej lub rownej 5, korzystam z klasycznego
selection sorta, ktorego zlozonosc wynosi O(n^2). Dla wiekszej ilosci danych korzystam z quick sorta w wersji iteracyjnej
bez wykorzystania stosu. Polega ona na tym, ze po kazdym wykonaniu funkcji partition, dodaje znak '-' na poczatek napisu,
ktory znajduje sie pod prawym (r) indeksem tablicy ktora sortuje - czyli najpierw wywoluje partition(l,r), a potem dodaje
'-' na poczatek napisu pod array[r]. Jednoczesnie funkcja partition zwraca nam indeks kolejnego r. Wykonuje te dzialania
dopoki l < r. Po wyjsciu z tej petli, l jest zwiekszane o 1, natomiast r jest wyszukiwane za pomoca funkcji, ktora zwraca
pierwszy element tablicy ze znakiem minusa na poczatku. Nastepnie znak minusa jest kasowany z tego napisu i cala funkcja
sie powtarza.
Kazde wywolanie funkcji partition tworzy nam dodatkowy element z minusem, czyli dodatkowe r, dla ktorego musimy posortowac
fragment (l..r), z tego powodu za kazdym razem kiedy dodaje ten znak, zwiekszam rowniez 'i'. Zmniejszane jest ono natomiast
przy kazdym wejsciu w 'glowna' petle, gdyz kazde wejscie w petle (poza pierwszym) wiaze sie z wczesniejszym znalezieniem
nowego r i usunieciem jednego znaku '-'. Jest to wiec swoisty licznik elementow z tym znakiem.
Zakladajac ze kazda permutacja ciagu wejsciowego jest jednakowo prawdopodobna, zlozonosc tej wersji quicksorta jest
taka sama jak zlozonosc quicksorta rekurencyjnego czyli O(nlogn), poniewaz dzieli on tablice na 2 podzadania a wyznaczenie
ich dzieje sie w czasie liniowym.
*/

public class Source
{
    public static int numOfRows; // liczba wierszy tabeli
    public static int numOfColumns; // liczba kolumn tabeli
    public static int column; // kolumna wedlug ktorej odbywa sie sortowanie
    public static int order; // kolejnosc sortowania (rosnoco lub malejaco)
    public static String[][] data; // tabela z danymi do posortowania
    public static boolean isNum; // zmienna mowiaca o tym czy dane wedlug ktorych ma odbywac sie sortowanie to napisy czy liczby

    public static Scanner sc = new Scanner(System.in);
    public static void main(String[] args)
    {
        int sets = sc.nextInt(); sc.nextLine(); // liczba zestawow oraz przejscie do nastepnej linii
        for(int i = 0; i < sets; i++)
        {
            String[] RCO = sc.nextLine().split(","); // czytamy linie oraz rozdzielamy ja znakiem ',' do tabeli 3-elementowej

            numOfRows = Integer.parseInt(RCO[0]) + 1; // pierwszy element tabeli to liczba wierszy (nie liczac naglowka)
            column = Integer.parseInt(RCO[1]) - 1; // drugi element to kolumna wedlug ktorej sortujemy
            order = Integer.parseInt(RCO[2]); // trzeci element to porzadek sortowania

            data = new String[numOfRows][]; // inicjalizujemy tablice o zadanej liczbie wierszy
            for(int j = 0; j < numOfRows; j++)
            {
                data[j] = sc.nextLine().split(","); // czytamy kazdy wiersz z wykorzystaniem metody split po znaku ','
            }
            numOfColumns = data[0].length; // liczba kolumn to dlugosc dowolnego wiersza tabeli

            // aby sprawdzic czy dane w kolumnie wedlug ktorej sortujemy to liczby czy napisy, wystarczy sprawdzic czy
            // pierwszy znak pierwszego elementu tej kolumny jest cyfra czy nie
            isNum = Character.isDigit(data[1][column].charAt(0));

            Sort(); // sortujemy

            for(int j = 0; j < numOfRows; j++) // wypisanie posortowanej tablicy
            {
                System.out.print(data[j][column]); // na poczatku kazdego wiersza wypisujemy dane z kolumny wg ktorej sortowalismy
                if(numOfColumns != 1) System.out.print(','); // jesli byla wiecej niz jedna kolumna to dodajemy znak ','
                for(int k = 0; k < numOfColumns; k++) // wypisujemy kazdy kolejny wyraz, poza tym z kolumny wg ktorej sortowalismy
                {
                    if(k != column)
                    {
                        System.out.print(data[j][k]);
                        // dodajemy znak ',' tylko jesli wypisywany wyraz nie jest ostatni w wierszu
                        if(k != numOfColumns - 1 && (k != numOfColumns - 2 || column != numOfColumns - 1))
                        {
                            System.out.print(',');
                        }
                    }
                }
                System.out.println(); // nowa linia po kazdym wierszu
            }
            System.out.println(); // dodatkowa nowa linia po kazdym zestawie
        }
    }

    public static void Sort()
    {
        // jesli ilosc elementow wieksza od 5 to wywolujemy quick sort, w innym przypadku od razu mozemy wywolac selection sort
        if(numOfRows > 5)
        {
            quickSort();
        }
        else
        {
            if(order == 1) selectionSortAsc(1, numOfRows - 1);
            else selectionSortDesc(1, numOfRows - 1);
        }
    }

    private static void swapRows(int i, int j) // zamiana miejscami wierszy o indeksach i, j
    {
        String[] temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    private static int partitionAsc(int l, int r) // partition w wersji rosnacej
    {
        if(isNum)
        {
            // jesli jest liczba to parsujemy na inta i porownojemy za pomoca operatorow porownania
            int pivot = Integer.parseInt(data[(l+r)/2][column]);
            while (l <= r)
            {
                while (Integer.parseInt(data[l][column]) < pivot)
                    l++;
                while (Integer.parseInt(data[r][column]) > pivot)
                    r--;
                if (l <= r)
                {
                    swapRows(l++, r--);
                }
            }
        }
        else
        {
            // jesli jest napisem to porownujemy za pomoca metody compareTo
            String pivot = data[(l+r)/2][column];
            while (l <= r)
            {
                while (data[l][column].compareTo(pivot) < 0)
                    l++;
                while (data[r][column].compareTo(pivot) > 0)
                    r--;
                if (l <= r)
                {
                    swapRows(l++, r--);
                }
            }
        }
        return l;
    }

    private static int partitionDesc(int l, int r) // partition w wersji malejacej
    {
        if(isNum)
        {
            int pivot = Integer.parseInt(data[(l+r)/2][column]);
            while (l <= r)
            {
                while (Integer.parseInt(data[l][column]) > pivot)
                    l++;
                while (Integer.parseInt(data[r][column]) < pivot)
                    r--;
                if (l <= r)
                {
                    swapRows(l++, r--);
                }
            }
        }
        else
        {
            String pivot = data[(l+r)/2][column];
            while (l <= r)
            {
                while (data[l][column].compareTo(pivot) > 0)
                    l++;
                while (data[r][column].compareTo(pivot) < 0)
                    r--;
                if (l <= r)
                {
                    swapRows(l++, r--);
                }
            }
        }
        return l;
    }

    private static void quickSort()
    {
        int l = 1; // zaczynamy od indeksu 1, bo pod indeksem 0 sa naglowki
        int r = numOfRows - 1; // rozmiar - 1
        int q, i = 0; // q to po prostu zmienna pomocnicza, natomiast za pomoca i sprawdzamy warunek zakonczenia petli
        while(true)
        {
            i--; // zmniejszamy i, bo usunelismy jeden koniec podtablicy do posortowania
            while(l < r)
            {
                // jesli sortowana podtablica ma 5 lub mniej elementow to korzystamy z selection sorta, po ktorym od razu
                // ustawiamy r na l, bo wiadomo ze odcinek jest juz calkowicie posortowany
                if(r - l <= 4)
                {
                    if(order == 1) selectionSortAsc(l, r); // jesli order == 1 to sortujemy rosnaco
                    else selectionSortDesc(l, r); // w innym wypadku malejaco
                    data[r][column] = "-" + data[r][column]; // dodajemy znak '-'
                    r = l;
                }
                else
                {
                    if(order == 1) q = partitionAsc(l, r);
                    else q = partitionDesc(l, r);
                    data[r][column] = "-" + data[r][column];
                    r = q - 1;
                }
                i++;
            }

            // jesli wejdziemy w ponizszego ifa to znaczy ze posortowalismy cala tablice (liczba elementow ze znakiem '-'
            // jest rowna 0 oraz l == r) wiec konczymy
            if(i < 0) break;
            l++;
            r = findR(l);
            data[r][column] = data[r][column].substring(1);
        }
    }

    private static int findR(int l) // szukanie kolejnego elementu, wyznaczajacego koniec podtablicy do posortowania
    {
        for (int i = l; i < numOfRows; ++i)
        {
            if (data[i][column].charAt(0) == '-')
                return i;
        }
        return numOfRows - 1;
    }

    private static void selectionSortAsc(int low, int high) // selection sort rosnaco
    {
        for (int i = low; i < high; i++)
        {
            int minIndex = i;
            for (int j = i + 1; j <= high; j++)
            {
                if(isNum)
                {
                    if (Integer.parseInt(data[j][column]) < Integer.parseInt(data[minIndex][column]))
                    {
                        minIndex = j;
                    }
                }
                else
                {
                    if (data[j][column].compareTo(data[minIndex][column]) < 0)
                    {
                        minIndex = j;
                    }
                }
            }
            swapRows(i, minIndex);
            data[i][column] = data[i][column];
        }
    }

    private static void selectionSortDesc(int low, int high) // selection sort malejaco
    {
        for (int i = low; i < high; i++)
        {
            int maxIndex = i;
            for (int j = i + 1; j <= high; j++)
            {
                if(isNum)
                {
                    if (Integer.parseInt(data[j][column]) > Integer.parseInt(data[maxIndex][column]))
                    {
                        maxIndex = j;
                    }
                }
                else
                {
                    if (data[j][column].compareTo(data[maxIndex][column]) > 0)
                    {
                        maxIndex = j;
                    }
                }
            }
            swapRows(i, maxIndex);
            data[i][column] = data[i][column];
        }
    }
}

/*
INPUT:
8
10,1,1
LA,LB,DO,DI
pa,pb,250,251
fa,fb,150,151
wa,wb,320,321
qa,qb,260,261
da,db,130,131
oa,ob,240,241
la,lb,210,211
ja,jb,190,191
ua,ub,300,301
aa,ab,100,101
10,2,1
LA,LB,DO,DI
pa,pb,250,251
fa,fb,150,151
wa,wb,320,321
qa,qb,260,261
da,db,130,131
oa,ob,240,241
la,lb,210,211
ja,jb,190,191
ua,ub,300,301
aa,ab,100,101
10,1,-1
LA,LB,DO,DI
pa,pb,250,251
fa,fb,150,151
wa,wb,320,321
qa,qb,260,261
da,db,130,131
oa,ob,240,241
la,lb,210,211
ja,jb,190,191
ua,ub,300,301
aa,ab,100,101
10,2,-1
LA,LB,DO,DI
pa,pb,250,251
fa,fb,150,151
wa,wb,320,321
qa,qb,260,261
da,db,130,131
oa,ob,240,241
la,lb,210,211
ja,jb,190,191
ua,ub,300,301
aa,ab,100,101
10,3,1
LA,LB,DO,DI
pa,pb,250,251
fa,fb,150,151
wa,wb,320,321
qa,qb,260,261
da,db,130,131
oa,ob,240,241
la,lb,210,211
ja,jb,190,191
ua,ub,300,301
aa,ab,100,101
10,4,1
LA,LB,DO,DI
pa,pb,250,251
fa,fb,150,151
wa,wb,320,321
qa,qb,260,261
da,db,130,131
oa,ob,240,241
la,lb,210,211
ja,jb,190,191
ua,ub,300,301
aa,ab,100,101
10,3,-1
LA,LB,DO,DI
pa,pb,250,251
fa,fb,150,151
wa,wb,320,321
qa,qb,260,261
da,db,130,131
oa,ob,240,241
la,lb,210,211
ja,jb,190,191
ua,ub,300,301
aa,ab,100,101
10,4,-1
LA,LB,DO,DI
pa,pb,250,251
fa,fb,150,151
wa,wb,320,321
qa,qb,260,261
da,db,130,131
oa,ob,240,241
la,lb,210,211
ja,jb,190,191
ua,ub,300,301
aa,ab,100,101

OUTPUT:
LA,LB,DO,DI
aa,ab,100,101
da,db,130,131
fa,fb,150,151
ja,jb,190,191
la,lb,210,211
oa,ob,240,241
pa,pb,250,251
qa,qb,260,261
ua,ub,300,301
wa,wb,320,321

LB,LA,DO,DI
ab,aa,100,101
db,da,130,131
fb,fa,150,151
jb,ja,190,191
lb,la,210,211
ob,oa,240,241
pb,pa,250,251
qb,qa,260,261
ub,ua,300,301
wb,wa,320,321

LA,LB,DO,DI
wa,wb,320,321
ua,ub,300,301
qa,qb,260,261
pa,pb,250,251
oa,ob,240,241
la,lb,210,211
ja,jb,190,191
fa,fb,150,151
da,db,130,131
aa,ab,100,101

LB,LA,DO,DI
wb,wa,320,321
ub,ua,300,301
qb,qa,260,261
pb,pa,250,251
ob,oa,240,241
lb,la,210,211
jb,ja,190,191
fb,fa,150,151
db,da,130,131
ab,aa,100,101

DO,LA,LB,DI
100,aa,ab,101
130,da,db,131
150,fa,fb,151
190,ja,jb,191
210,la,lb,211
240,oa,ob,241
250,pa,pb,251
260,qa,qb,261
300,ua,ub,301
320,wa,wb,321

DI,LA,LB,DO
101,aa,ab,100
131,da,db,130
151,fa,fb,150
191,ja,jb,190
211,la,lb,210
241,oa,ob,240
251,pa,pb,250
261,qa,qb,260
301,ua,ub,300
321,wa,wb,320

DO,LA,LB,DI
320,wa,wb,321
300,ua,ub,301
260,qa,qb,261
250,pa,pb,251
240,oa,ob,241
210,la,lb,211
190,ja,jb,191
150,fa,fb,151
130,da,db,131
100,aa,ab,101

DI,LA,LB,DO
321,wa,wb,320
301,ua,ub,300
261,qa,qb,260
251,pa,pb,250
241,oa,ob,240
211,la,lb,210
191,ja,jb,190
151,fa,fb,150
131,da,db,130
101,aa,ab,100
*/