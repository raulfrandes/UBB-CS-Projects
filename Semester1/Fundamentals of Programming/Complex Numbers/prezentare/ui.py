from domain.list_manager import setup_lista_manager, get_lista_curenta, undo, get_undo
from domain.complex_number import get_parte_reala, get_parte_imaginara, creeaza_numar_complex
from business.service import tipareste_parte_imaginara, tipareste_modul_sub_zece, tipareste_modul_egal_zece, \
    filtrare_parte_reala_prim, suma_numere, produs_numere, sortare_parte_imaginara, filtrare_modul
from infrastructure.repository import adauga_numar_complex_la_lista, inserare_numar_complex_la_lista, sterge_element, \
    sterge_interval_de_elemente, inlocuieste_elemente, copie_lista


def afisare_meniu():
    print("Meniu:")
    print(" Citire listă")
    print("1. Introduceti numerele complexe.")
    print(" Adaugă număr în listă")
    print("2. Adaugă număr complex la sfârșitul listei.")
    print("3. Inserare număr complex pe o poziție dată.")
    print(" Modifică elemente din listă")
    print("4. Șterge element de pe o poziție dată.")
    print("5. Șterge elementele de pe un interval de poziții.")
    print("6. Înlocuiește toate aparițiile unui număr complex cu un alt număr complex.")
    print(" Căutare numere")
    print("7. Tipărește partea imaginara pentru numerele din listă. Se dă intervalul de poziții.")
    print("8. Tipărește toate numerele complexe care au modulul mai mic decât 10.")
    print("9. Tipărește toate numerele complexe care au modulul egal cu 10.")
    print(" Operații cu numerele din listă")
    print("10. Suma numerelor dintr-o subsecventă dată.")
    print("11. Produsul numerelor dintr-o subsecventă dată.")
    print("12. Tipărește lista sortată descrescător după partea imaginara.")
    print(" Filtrare")
    print("13. Elimină din listă numerele complexe la care partea reala este numar prim.")
    print("14. Elimina din lista numerele complexe la care modulul este <,= sau > decât un număr dat.")
    print(" Undo")
    print("15. Undo")
    print(" Ieșire")
    print("16. Iesire.")


def convertire_lista(l_memorie):
    l_afisata = []
    for i in l_memorie:
        l_afisata.append(complex(get_parte_reala(i), get_parte_imaginara(i)))
    return l_afisata


def citeste_numere():
    l_memorie = []
    n = int(input("Introduceti numarul de elemente: "))
    for i in range(n):
        print("numar ", i+1)
        parte_reala = int(input("Introduceti partea reala: "))
        parte_imaginara = int(input("Introduceti parte imaginara: "))
        numar_complex = creeaza_numar_complex(parte_reala, parte_imaginara)
        l_memorie.append(numar_complex)
    return l_memorie


def adauga_numar_complex_la_lista_ui(l):
    print("Ce numar complex doriti sa adaugati?")
    parte_reala = int(input("Introduceti partea reala: "))
    parte_imaginara = int(input("Introduceti parte imaginara: "))
    numar_complex = creeaza_numar_complex(parte_reala, parte_imaginara)
    adauga_numar_complex_la_lista(l, numar_complex)
    print(convertire_lista(l))



def inserare_numar_complex_la_lista_ui(l):
    print("Ce numar doriti sa inserati?")
    parte_reala = int(input("Introduceti partea reala: "))
    parte_imaginara = int(input("Introduceti parte imaginara: "))
    pozitie = int(input("Pe ce pozitie? "))
    numar_complex = creeaza_numar_complex(parte_reala, parte_imaginara)
    inserare_numar_complex_la_lista(l, numar_complex, pozitie-1)
    print(convertire_lista(l))


def sterge_element_ui(l):
    pozitie = int(input("Elemenetul de pe ce poztie doriti sa il stergeti? "))
    sterge_element(l, pozitie-1)
    print(convertire_lista(l))


def sterge_interval_de_elemente_ui(l):
    print("Din ce interval doriti sa stergeti?")
    inceput_interval = int(input("Introduceti inceputul intervalului: "))
    sfarsit_interval = int(input("Introduceti sfarsitul intervalului: "))
    sterge_interval_de_elemente(l, inceput_interval-1, sfarsit_interval-1)
    print(convertire_lista(l))


def inlocuieste_elemente_ui(l):
    print("Ce element doriti sa inlocuiti?")
    parte_reala = int(input("Introduceti partea reala: "))
    parte_imaginara = int(input("Introduceti parte imaginara: "))
    numar_initial = creeaza_numar_complex(parte_reala, parte_imaginara)
    print("Cu ce element doriti ca acesta sa fie inlocuit?")
    parte_reala_noua = int(input("Introduceti partea reala: "))
    parte_imaginara_noua = int(input("Introduceti parte imaginara: "))
    numar_nou = creeaza_numar_complex(parte_reala_noua, parte_imaginara_noua)
    inlocuieste_elemente(l, numar_initial, numar_nou)
    print(convertire_lista(l))


def tipareste_parte_imaginara_ui(l):
    print("Din ce interval doriti sa tipariti?")
    inceput_interval = int(input("Introduceti inceputul intervalului: "))
    sfarsit_interval = int(input("Introduceti sfarsitul intervalului: "))
    print(tipareste_parte_imaginara(l, inceput_interval-1, sfarsit_interval-1))


def tipareste_modul_sub_zece_ui(l):
    print(convertire_lista(tipareste_modul_sub_zece(l)))


def tipareste_modul_egal_zece_ui(l):
    print(convertire_lista(tipareste_modul_egal_zece(l)))


def filtrare_parte_reala_prim_ui(l):
    filtrare_parte_reala_prim(l)
    print(convertire_lista(l))


def suma_numere_ui(l):
    rezultat = []
    print("Din ce interval doriti sa tipariti?")
    inceput_interval = int(input("Introduceti inceputul intervalului: "))
    sfarsit_interval = int(input("Introduceti sfarsitul intervalului: "))
    rezultat.append(suma_numere(l, inceput_interval-1, sfarsit_interval-1))
    print(convertire_lista(rezultat))


def produs_numere_ui(l):
    rezultat = []
    print("Din ce interval doriti sa tipariti?")
    inceput_interval = int(input("Introduceti inceputul intervalului: "))
    sfarsit_interval = int(input("Introduceti sfarsitul intervalului: "))
    rezultat.append(produs_numere(l, inceput_interval - 1, sfarsit_interval - 1))
    print(convertire_lista(rezultat))


def sortare_parte_imaginara_ui(l):
    sortare_parte_imaginara(l)
    print(convertire_lista(l))


def filtrare_modul_ui(l):
    optiune = input("Optiunea dumneavoastra este(<, =, >): ")
    numar = float(input("Introduceti numarul de comparat: "))
    filtrare_modul(l, optiune, numar)
    print(convertire_lista(l))


def undo_ui(lista_manager):
    undo(lista_manager)
    print(convertire_lista(get_lista_curenta(lista_manager)))


def adauga_numar_complex_la_lista_ui_comanda(l, params):
    if len(params) != 2:
        print("numar parametrii invalid!")
        return
    parte_reala = int(params[0])
    parte_imaginara = int(params[1])
    numar_complex = creeaza_numar_complex(parte_reala, parte_imaginara)
    adauga_numar_complex_la_lista(l, numar_complex)


def sterge_element_ui_comanda(l, params):
    if len(params) != 1:
        print("numar parametrii invalid!")
        return
    pozitie = int(params[0])
    sterge_element(l, pozitie - 1)


def print_comanda(l, params):
    print(convertire_lista(l))


def start():
    afisare_meniu()
    while True:
        optiune_input = input("Introduceti tipul de input: ")
        if optiune_input == "int":
            while True:
                optiune = int(input("Optiunea dumneavoastra este: "))
                if optiune == 1:
                    l = citeste_numere()
                    lista_manager = setup_lista_manager(l)
                    print(convertire_lista(get_lista_curenta(lista_manager)))
                elif optiune == 2:
                    lista_curenta = get_lista_curenta(lista_manager)
                    undo_list = get_undo(lista_manager)
                    undo_list.append(copie_lista(lista_curenta))
                    adauga_numar_complex_la_lista_ui(lista_curenta)
                elif optiune == 3:
                    lista_curenta = get_lista_curenta(lista_manager)
                    undo_list = get_undo(lista_manager)
                    undo_list.append(copie_lista(lista_curenta))
                    inserare_numar_complex_la_lista_ui(lista_curenta)
                elif optiune == 4:
                    lista_curenta = get_lista_curenta(lista_manager)
                    undo_list = get_undo(lista_manager)
                    undo_list.append(copie_lista(lista_curenta))
                    sterge_element_ui(lista_curenta)
                elif optiune == 5:
                    lista_curenta = get_lista_curenta(lista_manager)
                    undo_list = get_undo(lista_manager)
                    undo_list.append(copie_lista(lista_curenta))
                    sterge_interval_de_elemente_ui(lista_curenta)
                elif optiune == 6:
                    lista_curenta = get_lista_curenta(lista_manager)
                    undo_list = get_undo(lista_manager)
                    undo_list.append(copie_lista(lista_curenta))
                    inlocuieste_elemente_ui(lista_curenta)
                elif optiune == 7:
                    lista_curenta = get_lista_curenta(lista_manager)
                    tipareste_parte_imaginara_ui(lista_curenta)
                elif optiune == 8:
                    lista_curenta = get_lista_curenta(lista_manager)
                    tipareste_modul_sub_zece_ui(lista_curenta)
                elif optiune == 9:
                    lista_curenta = get_lista_curenta(lista_manager)
                    tipareste_modul_egal_zece_ui(lista_curenta)
                elif optiune == 10:
                    lista_curenta = get_lista_curenta(lista_manager)
                    suma_numere_ui(lista_curenta)
                elif optiune == 11:
                    lista_curenta = get_lista_curenta(lista_manager)
                    produs_numere_ui(lista_curenta)
                elif optiune == 12:
                    lista_curenta = get_lista_curenta(lista_manager)
                    undo_list = get_undo(lista_manager)
                    undo_list.append(copie_lista(lista_curenta))
                    sortare_parte_imaginara_ui(lista_curenta)
                elif optiune == 13:
                    lista_curenta = get_lista_curenta(lista_manager)
                    undo_list = get_undo(lista_manager)
                    undo_list.append(copie_lista(lista_curenta))
                    filtrare_parte_reala_prim_ui(lista_curenta)
                elif optiune == 14:
                    lista_curenta = get_lista_curenta(lista_manager)
                    undo_list = get_undo(lista_manager)
                    undo_list.append(copie_lista(lista_curenta))
                    filtrare_modul_ui(lista_curenta)
                elif optiune == 15:
                    undo_ui(lista_manager)
                elif optiune == 16:
                    return
        elif optiune_input == "comanda":
            while True:
                comenzi = {
                    "adauga": adauga_numar_complex_la_lista_ui_comanda,
                    "sterge": sterge_element_ui_comanda,
                    "undo": undo_ui
                }
                sir_comenzi = input(">>>")
                if sir_comenzi == "":
                    continue
                elif sir_comenzi == "exit":
                    return
                elif sir_comenzi == "citire":
                    l = citeste_numere()
                    lista_manager = setup_lista_manager(l)
                elif sir_comenzi == "undo":
                    undo_ui(lista_manager)
                else:
                    sir_comanda = sir_comenzi.split(';')
                    for i in sir_comanda:
                        element_comanda = i.split()
                        nume_comanda = str(element_comanda[0])
                        params = element_comanda[1:]
                        if nume_comanda in comenzi:
                            try:
                                lista_curenta = get_lista_curenta(lista_manager)
                                undo_list = get_undo(lista_manager)
                                undo_list.append(copie_lista(lista_curenta))
                                comenzi[nume_comanda](l, params)
                            except ValueError as ve:
                                print(ve)
                    print(convertire_lista(l))