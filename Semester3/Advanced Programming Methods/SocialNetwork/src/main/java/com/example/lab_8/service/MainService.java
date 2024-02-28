package com.example.lab_8.service;

import com.example.lab_8.domain.*;
import com.example.lab_8.repository.PagingFriendshipRepository;
import com.example.lab_8.repository.Repository;
import com.example.lab_8.repository.paging.Page;
import com.example.lab_8.repository.paging.PagingRepository;
import com.example.lab_8.repository.paging.Pegeable;
import com.example.lab_8.utils.events.ChangeEventType;
import com.example.lab_8.utils.events.FriendshipChangeEvent;
import com.example.lab_8.utils.events.UserChangeEvent;
import com.example.lab_8.utils.observer.Observable;
import com.example.lab_8.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class MainService implements Service {
    private final PagingRepository<Long, User> userRepository;
    private final PagingFriendshipRepository friendshipRepository;
    private Set<User> set;
    public MainService(PagingRepository<Long, User> userRepository, PagingFriendshipRepository friendshipRepository){
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    public Page<User> getUsersOnPage(Pegeable pegeable){
        return userRepository.findAll(pegeable);
    }



    private List<User> getFriends(User user) {
        Iterable<Friendship> allFriends = friendshipRepository.findAll();
        return StreamSupport.stream(allFriends.spliterator(), false)
                .filter(friendship -> friendship.getUser1().equals(user))
                .map(Friendship::getUser2)
                .toList();
    }

    @Override
    public Optional<User> addUser(String username, String password, String firstName, String lastName) {
        User user = new User(username, password, firstName, lastName);
        user.setPasswordWithSalt(password);
        Optional<User> addedUser = userRepository.save(user);
        if (addedUser.isEmpty()){
            UserChange().notifyObservers(new UserChangeEvent(ChangeEventType.ADD, null));
        }
        return addedUser;
    }

    @Override
    public Optional<User> deleteUser(Long id) {
        Optional<User> user = userRepository.delete(id);
        user.ifPresent(value -> UserChange().notifyObservers(new UserChangeEvent(ChangeEventType.DELETE, value)));
        return user;
    }

    public Optional<User> updateUser(Long id, String newUsername, String newPassword, String newFirstName, String newLastName) {
        Optional<User> oldUser = userRepository.findOne(id);
        if (oldUser.isPresent()) {
            User newUser = new User(newUsername, newPassword, newFirstName, newLastName);
            newUser.setId(id);
            Optional<User> user = userRepository.update(newUser);
            UserChange().notifyObservers(new UserChangeEvent(ChangeEventType.UPDATE, newUser, oldUser.get()));
            return user;
        }
        return oldUser;
    }

    @Override
    public Optional<Friendship> addFriendship(Long id1, Long id2) {
        User user1 = userRepository.findOne(id1)
                .orElseThrow(() -> new ServiceException("User does not exist."));

        User user2 = userRepository.findOne(id2)
                .orElseThrow(() -> new ServiceException("User does not exist."));

        if (getFriends(user1).contains(user2)) {
            throw new IllegalArgumentException("Friendship already added.");
        }

        Friendship friendship = new Friendship(user1, user2, LocalDateTime.now(), FriendRequest.PENDING);
        Optional<Friendship> savedFriendship =  friendshipRepository.save(friendship);
        if (savedFriendship.isEmpty()) {
            FriendshipChange().notifyObservers(new FriendshipChangeEvent(ChangeEventType.ADD, null));
        }
        return savedFriendship;
    }


    @Override
    public Optional<Friendship> deleteFriendship(Long id1, Long id2) {
        Tuple<Long, Long> id = new Tuple<>(id1, id2);
        Optional<Friendship> friendship = friendshipRepository.delete(id);
        if (friendship.isEmpty()){
            Tuple<Long, Long> idReversed = new Tuple<>(id2, id1);
            Optional<Friendship> friendship2 = friendshipRepository.delete(idReversed);
            if (friendship2.isEmpty()){
                throw new ServiceException("friendship does not exist");
            }
            else {
                FriendshipChange().notifyObservers(new FriendshipChangeEvent(ChangeEventType.DELETE, friendship2.get()));
            }
            return friendship2;
        }
        else {
            FriendshipChange().notifyObservers(new FriendshipChangeEvent(ChangeEventType.DELETE, friendship.get()));
        }
        return friendship;
    }

    public Optional<Friendship> updateFriendship(Tuple<Long, Long> id, LocalDateTime newFriendFrom, FriendRequest newStatus) {
        Optional<Friendship> oldFriendship = friendshipRepository.findOne(id);
        if (oldFriendship.isPresent()) {
            User user1 = userRepository.findOne(id.getLeft())
                    .orElseThrow(() -> new ServiceException("User does not exist!"));
            User user2 = userRepository.findOne(id.getRight())
                    .orElseThrow(() -> new ServiceException("User does not exist!"));
            Friendship newFriendship = new Friendship(user1, user2, newFriendFrom, newStatus);
            Optional<Friendship> friendship = friendshipRepository.update(newFriendship);
            if (friendship.isEmpty()) {
                FriendshipChange().notifyObservers(new FriendshipChangeEvent(ChangeEventType.UPDATE, newFriendship, oldFriendship.get()));
                return oldFriendship;
            }
            return friendship;
        }
        return oldFriendship;
    }

    private void DFS(User user, ArrayList<User> community) {
        set.add(user);
        community.add(user);

        getFriends(user)
                .stream()
                .filter(friend -> !set.contains(friend))
                .forEach(friend -> DFS(friend, community));
    }


    @Override
    public Integer getNumberOfCommunities() {
        set = new HashSet<User>();
        Iterable<User> users = userRepository.findAll();
        List<List<User>> communities = StreamSupport.stream(users.spliterator(), false)
                .filter(user -> !set.contains(user))
                .map(user -> {
                    ArrayList<User> community = new ArrayList<>();
                    DFS(user, community);
                    return community;
                })
                .collect(Collectors.toList());

        return communities.size();
    }


    public List<List<User>> getCommunities() {
        Iterable<User> users = userRepository.findAll();

        set = new HashSet<>();  // Set pentru a urmări utilizatorii vizitați

        return StreamSupport.stream(users.spliterator(), false)
                .filter(user -> !set.contains(user))
                .map(user -> {
                    ArrayList<User> community = new ArrayList<>();
                    DFS(user, community);
                    return community;
                })
                .collect(Collectors.toList());
    }

    private List<Observer<UserChangeEvent>> observers = new ArrayList<>();

    public Observable<UserChangeEvent> UserChange() {
        return new Observable<UserChangeEvent>() {

            @Override
            public void addObserver(Observer<UserChangeEvent> e) {
                observers.add(e);
            }

            @Override
            public void removeObserver(Observer<UserChangeEvent> e) {
                observers.remove(e);
            }

            @Override
            public void notifyObservers(UserChangeEvent t) {
                observers.forEach(x -> x.update(t));
            }
        };
    }

    private List<Observer<FriendshipChangeEvent>> observersF = new ArrayList<>();

    public Observable<FriendshipChangeEvent> FriendshipChange() {
        return new Observable<FriendshipChangeEvent>() {
            @Override
            public void addObserver(Observer<FriendshipChangeEvent> e) {
                observersF.add(e);
            }

            @Override
            public void removeObserver(Observer<FriendshipChangeEvent> e) {
                observersF.remove(e);
            }

            @Override
            public void notifyObservers(FriendshipChangeEvent t) {
                observersF.forEach(x -> x.update(t));
            }
        };
    }

    static class Pair<F, S>{
        public final F first;
        public final S second;
        public Pair(F first, S second){
            this.first = first;
            this.second = second;
        }
    }

    private Pair<Integer, Integer> BFS(int s, int numberOfUsers, LinkedList<Integer>[] adj) {
        int[] dis = new int[numberOfUsers];
        Arrays.fill(dis, -1);

        Queue<Integer> q = new LinkedList<>();
        q.add(s);
        dis[s] = 0;

        while (!q.isEmpty()) {
            int t = q.poll();
            adj[t].stream()
                    .filter(v -> dis[v] == -1)
                    .forEach(v -> {
                        q.add(v);
                        dis[v] = dis[t] + 1;
                    });
        }

        int maxDis = 0;
        int nodeIdx = 0;
        for (int i = 0; i < numberOfUsers; ++i){
            if (dis[i] > maxDis){
                maxDis = dis[i];
                nodeIdx = i;
            }
        }
        return new Pair<Integer, Integer>(nodeIdx, maxDis);
    }


    private Integer longestPathLength(List<User> community) {
        int numberOfUsers = community.size();
        LinkedList<Integer>[] adj = new LinkedList[numberOfUsers];

        IntStream.range(0, numberOfUsers)
                .forEach(i -> adj[i] = new LinkedList<>());

        community.forEach(user ->
                getFriends(user).forEach(friend ->
                        adj[community.indexOf(user)].add(community.indexOf(friend))
                )
        );

        Pair<Integer, Integer> t1 = BFS(0, numberOfUsers, adj);
        Pair<Integer, Integer> t2 = BFS(t1.first, numberOfUsers, adj);

        return t2.second;
    }


    @Override
    public List<List<User>> getTheMostSociableCommunity() {
        List<List<User>> communities = getCommunities();

        OptionalInt maxCommunitySize = communities.stream()
                .mapToInt(this::longestPathLength)
                .max();

        return maxCommunitySize.isPresent()
                ? communities.stream()
                .filter(community -> longestPathLength(community) == maxCommunitySize.getAsInt())
                .collect(Collectors.toList())
                : Collections.emptyList();
    }


    public Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }

    public List<User> getStrangers(User user) {
        Iterable<User> users = userRepository.findAll();
        List<User> friends = getFriends(user);
        return StreamSupport.stream(users.spliterator(), false)
                .filter(user1 -> !friends.contains(user1))
                .toList();
    }

    public Page<FriendDTO> getUserFriends(Pegeable pegeable, User user){
        Page<Friendship> friendshipsOnPage = friendshipRepository.findAll(pegeable, user);
        Iterable<Friendship> allFriedshipsOnPage = friendshipsOnPage.getElementsOnPage();

        List<FriendDTO> friends = StreamSupport.stream(allFriedshipsOnPage.spliterator(), false)
                .filter(friendship -> friendship.getUser1().equals(user) ||
                                        friendship.getUser2().equals(user))
                .map(friendship -> {
                    if (friendship.getUser1().equals(user)){
                        return new FriendDTO(friendship.getUser2().getId(), friendship.getUser2().getUsername(), friendship.getUser2().getFirstName(),
                                friendship.getUser2().getLastName(), friendship.getStatus(),
                                friendship.getFriendsFrom());
                    }
                    else {
                        return new FriendDTO(friendship.getUser1().getId(), friendship.getUser1().getUsername(), friendship.getUser1().getFirstName(),
                                friendship.getUser1().getLastName(), friendship.getStatus(),
                                friendship.getFriendsFrom());
                    }
                })
                .toList();
        return new Page<>(friends, friendshipsOnPage.getTotalNumberOfElements());
    }

    public Optional<User> findOneUsername(String username) {
        Iterable<User> users = userRepository.findAll();
        return StreamSupport.stream(users.spliterator(), false)
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }
}
