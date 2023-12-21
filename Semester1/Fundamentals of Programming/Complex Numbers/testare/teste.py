# import math
#
#
#
# def test_creeaza_numar_complex():
#     parte_reala = 5
#     parte_imaginara = 7
#     numar_complex = creeaza_numar_complex(parte_reala, parte_imaginara)
#     assert (parte_reala == get_parte_reala(numar_complex))
#     assert (parte_imaginara == get_parte_imaginara(numar_complex))
#
#
# def test_adaugare_numar_complex_la_lista():
#     lista_test = []
#     numar_complex = creeaza_numar_complex(5, 6)
#     adauga_numar_complex_la_lista(lista_test, numar_complex)
#     assert (len(lista_test) == 1)
#     assert (get_parte_reala(lista_test[0]) == 5)
#     assert (get_parte_imaginara(lista_test[0]) == 6)
#
#
# def test_inserare_numar_complex_la_lista():
#     lista_test = []
#     numar_complex1 = creeaza_numar_complex(5, 6)
#     numar_complex2 = creeaza_numar_complex(7, 3)
#     adauga_numar_complex_la_lista(lista_test, numar_complex1)
#     adauga_numar_complex_la_lista(lista_test, numar_complex2)
#     numar_complex3 = creeaza_numar_complex(4, 2)
#     pozitie = 1
#     inserare_numar_complex_la_lista(lista_test, numar_complex3, pozitie)
#     assert (len(lista_test) == 3)
#     assert (get_parte_reala(lista_test[pozitie]) == 4)
#     assert (get_parte_imaginara(lista_test[pozitie]) == 2)
#
#     pozitie_gresita = 6
#     try:
#         inserare_numar_complex_la_lista(lista_test, numar_complex3, pozitie_gresita)
#         assert False
#     except ValueError as ve:
#         assert (str(ve) == "pozitie invalida!\n")
#
#
# def test_sterge_element():
#     lista_test = []
#     numar_complex1 = creeaza_numar_complex(13, 6)
#     numar_complex2 = creeaza_numar_complex(19, 5)
#     numar_complex3 = creeaza_numar_complex(1, 2)
#     adauga_numar_complex_la_lista(lista_test, numar_complex1)
#     adauga_numar_complex_la_lista(lista_test, numar_complex2)
#     adauga_numar_complex_la_lista(lista_test, numar_complex3)
#     pozitie = 1
#     sterge_element(lista_test, pozitie)
#     assert (len(lista_test) == 2)
#     assert (get_parte_reala(lista_test[pozitie]) == 1)
#     assert (get_parte_imaginara(lista_test[pozitie]) == 2)
#
#     pozitie_gresita = 7
#     try:
#         sterge_element(lista_test, pozitie_gresita)
#         assert False
#     except ValueError as ve:
#         assert (str(ve) == "pozitie invalida!\n")
#
#
# def test_sterge_interval_elemente():
#     lista_test = []
#     numar_complex1 = creeaza_numar_complex(1, 3)
#     numar_complex2 = creeaza_numar_complex(3, 7)
#     numar_complex3 = creeaza_numar_complex(5, 9)
#     numar_complex4 = creeaza_numar_complex(7, 10)
#     numar_complex5 = creeaza_numar_complex(9, 11)
#     adauga_numar_complex_la_lista(lista_test, numar_complex1)
#     adauga_numar_complex_la_lista(lista_test, numar_complex2)
#     adauga_numar_complex_la_lista(lista_test, numar_complex3)
#     adauga_numar_complex_la_lista(lista_test, numar_complex4)
#     adauga_numar_complex_la_lista(lista_test, numar_complex5)
#     inceput_interval = 1
#     sfarsit_interval = 3
#     sterge_interval_de_elemente(lista_test, inceput_interval, sfarsit_interval)
#     assert (len(lista_test) == 2)
#     assert (get_parte_reala(lista_test[inceput_interval]) == 9)
#     assert (get_parte_imaginara(lista_test[inceput_interval]) == 11)
#
#     inceput_interval_gresit = -3
#     sfarsit_interval_gresit = 9
#     try:
#         sterge_interval_de_elemente(lista_test, inceput_interval_gresit, sfarsit_interval_gresit)
#         assert False
#     except ValueError as ve:
#         assert (str(ve) == "interval invalid!\n")
#
#
# def test_inlocuieste_elemente():
#     lista_test = []
#     numar_complex1 = creeaza_numar_complex(1, 2)
#     numar_complex2 = creeaza_numar_complex(3, 4)
#     numar_complex3 = creeaza_numar_complex(5, 6)
#     numar_complex4 = creeaza_numar_complex(3, 4)
#     numar_complex5 = creeaza_numar_complex(3, 4)
#     adauga_numar_complex_la_lista(lista_test, numar_complex1)
#     adauga_numar_complex_la_lista(lista_test, numar_complex2)
#     adauga_numar_complex_la_lista(lista_test, numar_complex3)
#     adauga_numar_complex_la_lista(lista_test, numar_complex4)
#     adauga_numar_complex_la_lista(lista_test, numar_complex5)
#     numar_initial = creeaza_numar_complex(3, 4)
#     numar_nou = creeaza_numar_complex(9, 7)
#     inlocuieste_elemente(lista_test, numar_initial, numar_nou)
#     assert (len(lista_test) == 5)
#     assert (lista_test.count(numar_nou) == 3)
#
#     numar_initial_gresit = creeaza_numar_complex(15, 15)
#     try:
#         inlocuieste_elemente(lista_test, numar_initial_gresit, numar_nou)
#         assert False
#     except ValueError as ve:
#         assert (str(ve) == "numar invalid!\n")
#
#
# def test_tipareste_parte_imaginara():
#     lista_test = []
#     numar_complex1 = creeaza_numar_complex(1, 2)
#     numar_complex2 = creeaza_numar_complex(3, 4)
#     numar_complex3 = creeaza_numar_complex(5, 6)
#     numar_complex4 = creeaza_numar_complex(7, 8)
#     numar_complex5 = creeaza_numar_complex(9, 10)
#     adauga_numar_complex_la_lista(lista_test, numar_complex1)
#     adauga_numar_complex_la_lista(lista_test, numar_complex2)
#     adauga_numar_complex_la_lista(lista_test, numar_complex3)
#     adauga_numar_complex_la_lista(lista_test, numar_complex4)
#     adauga_numar_complex_la_lista(lista_test, numar_complex5)
#     inceput_interval = 1
#     sfarsit_interval = 3
#     lista_imaginare = tipareste_parte_imaginara(lista_test, inceput_interval, sfarsit_interval)
#     assert (len(lista_imaginare) == 3)
#     assert (lista_imaginare[0] == get_parte_imaginara(numar_complex2))
#     assert (lista_imaginare[1] == get_parte_imaginara(numar_complex3))
#     assert (lista_imaginare[2] == get_parte_imaginara(numar_complex4))
#
#     inceput_interval_gresit = -3
#     sfarsit_interval_gresit = 11
#     try:
#         lista_imaginare = tipareste_parte_imaginara(lista_test, inceput_interval_gresit, sfarsit_interval_gresit)
#         assert False
#     except ValueError as ve:
#         assert (str(ve) == "interval invalid!\n")
#
#
# def test_modul():
#     numar_complex = creeaza_numar_complex(2, 5)
#     modul_numar_complex = modul(numar_complex)
#     assert (abs(modul_numar_complex - math.sqrt(29)) < 0.00001)
#
#
# def test_tipareste_modul_sub_10():
#     lista_test = []
#     numar_complex1 = creeaza_numar_complex(1, 2)
#     numar_complex2 = creeaza_numar_complex(3, 4)
#     numar_complex3 = creeaza_numar_complex(5, 6)
#     numar_complex4 = creeaza_numar_complex(7, 8)
#     numar_complex5 = creeaza_numar_complex(9, 10)
#     adauga_numar_complex_la_lista(lista_test, numar_complex1)
#     adauga_numar_complex_la_lista(lista_test, numar_complex2)
#     adauga_numar_complex_la_lista(lista_test, numar_complex3)
#     adauga_numar_complex_la_lista(lista_test, numar_complex4)
#     adauga_numar_complex_la_lista(lista_test, numar_complex5)
#     lista_module = tipareste_modul_sub_zece(lista_test)
#     assert (len(lista_module) == 3)
#     assert (lista_module[0] == numar_complex1)
#     assert (lista_module[1] == numar_complex2)
#     assert (lista_module[2] == numar_complex3)
#
#
# def test_tipareste_modul_egal_10():
#     lista_test = []
#     numar_complex1 = creeaza_numar_complex(1, 2)
#     numar_complex2 = creeaza_numar_complex(6, 8)
#     numar_complex3 = creeaza_numar_complex(5, 6)
#     numar_complex4 = creeaza_numar_complex(7, 8)
#     numar_complex5 = creeaza_numar_complex(8, -6)
#     adauga_numar_complex_la_lista(lista_test, numar_complex1)
#     adauga_numar_complex_la_lista(lista_test, numar_complex2)
#     adauga_numar_complex_la_lista(lista_test, numar_complex3)
#     adauga_numar_complex_la_lista(lista_test, numar_complex4)
#     adauga_numar_complex_la_lista(lista_test, numar_complex5)
#     lista_module = tipareste_modul_egal_zece(lista_test)
#     assert (len(lista_module) == 2)
#     assert (lista_module[0] == numar_complex2)
#     assert (lista_module[1] == numar_complex5)
#
#
# def test_numar_prim():
#     n = 5
#     assert(numar_prim(n) == True)
#
#
# def test_filtrare_parte_reala_prim():
#     lista_test = []
#     numar_complex1 = creeaza_numar_complex(5, 2)
#     numar_complex2 = creeaza_numar_complex(3, 4)
#     numar_complex3 = creeaza_numar_complex(8, 6)
#     numar_complex4 = creeaza_numar_complex(7, 8)
#     numar_complex5 = creeaza_numar_complex(9, 10)
#     adauga_numar_complex_la_lista(lista_test, numar_complex1)
#     adauga_numar_complex_la_lista(lista_test, numar_complex2)
#     adauga_numar_complex_la_lista(lista_test, numar_complex3)
#     adauga_numar_complex_la_lista(lista_test, numar_complex4)
#     adauga_numar_complex_la_lista(lista_test, numar_complex5)
#     filtrare_parte_reala_prim(lista_test)
#     assert(len(lista_test) == 2)
#     assert(get_parte_reala(lista_test[0]) == 8)
#     assert(get_parte_imaginara(lista_test[0]) == 6)
#     assert(get_parte_reala(lista_test[1]) == 9)
#     assert(get_parte_imaginara(lista_test[1]) == 10)
#
#
# def test_suma_numere():
#     lista_test = []
#     numar_complex1 = creeaza_numar_complex(5, 2)
#     numar_complex2 = creeaza_numar_complex(3, 4)
#     numar_complex3 = creeaza_numar_complex(8, 6)
#     numar_complex4 = creeaza_numar_complex(7, 8)
#     numar_complex5 = creeaza_numar_complex(9, 10)
#     adauga_numar_complex_la_lista(lista_test, numar_complex1)
#     adauga_numar_complex_la_lista(lista_test, numar_complex2)
#     adauga_numar_complex_la_lista(lista_test, numar_complex3)
#     adauga_numar_complex_la_lista(lista_test, numar_complex4)
#     adauga_numar_complex_la_lista(lista_test, numar_complex5)
#     inceput_interval = 1
#     sfarsit_interval = 3
#     parte_reala_rezultat = 18
#     parte_imaginara_rezultat = 18
#     rezultat = creeaza_numar_complex(parte_reala_rezultat, parte_imaginara_rezultat)
#     assert(suma_numere(lista_test, inceput_interval, sfarsit_interval) == rezultat)
#
#     inceput_interval_gresit = -3
#     sfarsit_interval_gresit = 80
#     try:
#         rezultat = suma_numere(lista_test, inceput_interval_gresit, sfarsit_interval_gresit)
#         assert False
#     except ValueError as ve:
#         assert(str(ve) == "interval invalid!\n")
#
#
# def test_produs_numere():
#     lista_test = []
#     numar_complex1 = creeaza_numar_complex(5, 2)
#     numar_complex2 = creeaza_numar_complex(3, 4)
#     numar_complex3 = creeaza_numar_complex(8, 6)
#     numar_complex4 = creeaza_numar_complex(7, 8)
#     numar_complex5 = creeaza_numar_complex(9, 10)
#     adauga_numar_complex_la_lista(lista_test, numar_complex1)
#     adauga_numar_complex_la_lista(lista_test, numar_complex2)
#     adauga_numar_complex_la_lista(lista_test, numar_complex3)
#     adauga_numar_complex_la_lista(lista_test, numar_complex4)
#     adauga_numar_complex_la_lista(lista_test, numar_complex5)
#     inceput_interval = 1
#     sfarsit_interval = 3
#     parte_reala_rezultat = -400
#     parte_imaginara_rezultat = 350
#     rezultat = creeaza_numar_complex(parte_reala_rezultat, parte_imaginara_rezultat)
#     assert(produs_numere(lista_test, inceput_interval, sfarsit_interval) == rezultat)
#
#     inceput_interval_gresit = -5
#     sfarsit_interval_gresit = 45
#     try:
#         rezultat = produs_numere(lista_test, inceput_interval_gresit, sfarsit_interval_gresit)
#         assert False
#     except ValueError as ve:
#         assert(str(ve) == "interval invalid!\n")
#
#
# def test_sortare_parte_imaginara():
#     lista_test = []
#     numar_complex1 = creeaza_numar_complex(5, 2)
#     numar_complex2 = creeaza_numar_complex(3, 6)
#     numar_complex3 = creeaza_numar_complex(8, 3)
#     adauga_numar_complex_la_lista(lista_test, numar_complex1)
#     adauga_numar_complex_la_lista(lista_test, numar_complex2)
#     adauga_numar_complex_la_lista(lista_test, numar_complex3)
#     sortare_parte_imaginara(lista_test)
#     assert(get_parte_reala(lista_test[0]) == 3)
#     assert(get_parte_imaginara(lista_test[0]) == 6)
#     assert(get_parte_reala(lista_test[1]) == 8)
#     assert(get_parte_imaginara(lista_test[1]) == 3)
#     assert(get_parte_reala(lista_test[2]) == 5)
#     assert(get_parte_imaginara(lista_test[2]) == 2)
#
#
# def test_filtrare_modul():
#     lista_test = []
#     numar_complex1 = creeaza_numar_complex(5, 2)
#     numar_complex2 = creeaza_numar_complex(3, 4)
#     numar_complex3 = creeaza_numar_complex(8, 6)
#     numar_complex4 = creeaza_numar_complex(7, 8)
#     numar_complex5 = creeaza_numar_complex(9, 10)
#     adauga_numar_complex_la_lista(lista_test, numar_complex1)
#     adauga_numar_complex_la_lista(lista_test, numar_complex2)
#     adauga_numar_complex_la_lista(lista_test, numar_complex3)
#     adauga_numar_complex_la_lista(lista_test, numar_complex4)
#     adauga_numar_complex_la_lista(lista_test, numar_complex5)
#     numar = 9
#     optiune = '<'
#     filtrare_modul(lista_test, optiune, numar)
#     assert(len(lista_test) == 3)
#     assert(get_parte_reala(lista_test[0]) == 8)
#     assert(get_parte_imaginara(lista_test[0]) == 6)
#     assert(get_parte_reala(lista_test[1]) == 7)
#     assert(get_parte_imaginara(lista_test[1]) == 8)
#     assert(get_parte_reala(lista_test[2]) == 9)
#     assert(get_parte_imaginara(lista_test[2]) == 10)
#     numar = 11
#     optiune = '>'
#     filtrare_modul(lista_test, optiune, numar)
#     assert(len(lista_test) == 2)
#     assert (get_parte_reala(lista_test[0]) == 8)
#     assert (get_parte_imaginara(lista_test[0]) == 6)
#     assert (get_parte_reala(lista_test[1]) == 7)
#     assert (get_parte_imaginara(lista_test[1]) == 8)
#     numar = 10
#     optiune = '='
#     filtrare_modul(lista_test, optiune, numar)
#     assert(len(lista_test) == 1)
#     assert (get_parte_reala(lista_test[0]) == 7)
#     assert (get_parte_imaginara(lista_test[0]) == 8)
#
#
# def test_copie_lista():
#     lista_test = []
#     numar_complex1 = creeaza_numar_complex(5, 2)
#     numar_complex2 = creeaza_numar_complex(3, 4)
#     adauga_numar_complex_la_lista(lista_test, numar_complex1)
#     adauga_numar_complex_la_lista(lista_test, numar_complex2)
#     copie = copie_lista(lista_test)
#     assert(get_parte_reala(copie[0]) == 5)
#     assert(get_parte_imaginara(copie[0]) == 2)
#     assert(get_parte_reala(copie[1]) == 3)
#     assert(get_parte_imaginara(copie[1]) == 4)
#
#
# def test_lista_manager():
#     lista_curenta = [[1, 2], [3, 4], [5, 6]]
#     lista_manager = setup_lista_manager(lista_curenta)
#     assert(get_lista_curenta(lista_manager) == [[1, 2], [3, 4], [5, 6]])
#     assert(get_undo(lista_manager) == [])
#     lista_precedenta = [[1, 2], [5, 6]]
#     undo_list = get_undo(lista_manager)
#     undo_list.append(copie_lista(lista_curenta))
#     set_lista_curenta(lista_manager, lista_precedenta)
#     assert(get_lista_curenta(lista_manager) == [[1, 2], [5, 6]])
#     assert(get_undo(lista_manager) == [[[1, 2], [3, 4], [5, 6]]])
#     undo(lista_manager)
#     assert(get_lista_curenta(lista_manager) == [[1, 2], [3, 4], [5, 6]])
#
#
# def ruleaza_toate_testele():
#     test_creeaza_numar_complex()
#     test_adaugare_numar_complex_la_lista()
#     test_inserare_numar_complex_la_lista()
#     test_sterge_element()
#     test_sterge_interval_elemente()
#     test_inlocuieste_elemente()
#     test_tipareste_parte_imaginara()
#     test_modul()
#     test_tipareste_modul_sub_10()
#     test_tipareste_modul_egal_10()
#     test_numar_prim()
#     test_filtrare_parte_reala_prim()
#     test_suma_numere()
#     test_produs_numere()
#     test_sortare_parte_imaginara()
#     test_filtrare_modul()
#     test_copie_lista()
#     test_lista_manager()
#     print("(Toate testele au fost trecute cu succes!)")
#
