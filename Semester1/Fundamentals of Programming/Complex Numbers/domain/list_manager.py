def listManagerSetup(current_list):
    """
    initialize an object of type list_manager
    :param current_list: list
    :return: res: list_manager - a list with two elements representing the list_manager:
                                    list_manager[0] is the current_list,
                                    list_manager[1] is the undo list
    """
    undo_list = []
    return [current_list, undo_list]


def get_current_list(list_manager):
    """
    returns the current list from the list_manager list
    :param list_manager: list_manager
    :return: res: list - the current list of the list_manager list
    """
    return list_manager[0]


def get_undo(list_manager):
    """
    returns the undo list from the list_manager list
    :param list_manager: list_manager
    :return: res: list - the undo list of th list_manager list
    """
    return list_manager[1]


def set_current_list(list_manager, previous_list):
    """
    modify the current list of the list_manager list with the previous_list list
    :param list_manager: list_manager
    :param previous_list: list
    """
    list_manager[0] = previous_list


def set_undo(list_manager, undo_list):
    """
    modify the undo list of the list_manager list with the undo_list list
    :param list_manager: list_manager
    :param undo_list: list
    """
    list_manager[1] = undo_list


def undo(list_manager):
    """
    undo the last operation
    :param list_manager: list_manager
    """
    undo_list = get_undo(list_manager)
    if len(undo_list) == 0:
        raise ValueError("Cannot undo anymore!")
    else:
        previous_list = undo_list[-1]
        set_current_list(list_manager, previous_list)
        set_undo(list_manager, undo_list[:-1])
