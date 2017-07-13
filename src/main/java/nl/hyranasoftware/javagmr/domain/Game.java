/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.hyranasoftware.javagmr.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Collections;
import static java.util.Collections.list;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.hyranasoftware.javagmr.threads.RetrievePlayers;
import org.joda.time.DateTime;
import org.ocpsoft.prettytime.PrettyTime;

/**
 *
 * @author danny_000
 */
public class Game implements Comparable<Game> {

    private int gameid;
    private String name;
    private List<Player> players;
    private CurrentTurn currentTurn;
    private int type;
    private DateTime uploaded;

    @JsonProperty("GameId")
    public int getGameid() {
        return gameid;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Players")
    public List<Player> getPlayers() {
        return players;
    }

    @JsonProperty("CurrentTurn")
    public CurrentTurn getCurrentTurn() {
        return currentTurn;
    }

    @JsonProperty("uploaded")
    public DateTime getUploaded() {
        return uploaded;
    }

    public void setUploaded(DateTime uploaded) {
        this.uploaded = uploaded;
    }

    @JsonProperty("Type")
    public int getType() {
        return type;
    }

    public void sortPlayers() {

        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                if (o1.turnOrder > o2.turnOrder) {
                    return 1;
                }
                return -1;
            }

        });
        int i = 0;
        ListIterator<Player> iterator = players.listIterator();
        List<Player> previousPlayers = new ArrayList();
        List<Player> nextPlayers = new ArrayList();
        while (iterator.hasNext()) {
            Player player = iterator.next();
            if (i >= currentTurn.playerNumber) {
                previousPlayers.add(player);
            } else {
                nextPlayers.add(player);
                System.out.println(iterator.nextIndex());
            }
            
            i++;

        }
        previousPlayers.addAll(nextPlayers);
        players = previousPlayers;
        
    }

    public void getPlayersFromGMR() {
        try {
            RetrievePlayers rp = new RetrievePlayers(players);
            ExecutorService executor = Executors.newFixedThreadPool(1);
            Future<List<Player>> future = executor.submit(rp);
            players = future.get();
            executor.shutdown();
        } catch (Exception ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getPrettyTimeLeft() {
        PrettyTime p = new PrettyTime();
        if (currentTurn.getExpires() != null) {
            return p.format(currentTurn.getExpires().toDate());
        } else {
            return p.format(currentTurn.getStarted().toDate());
        }
    }

    @Override
    public String toString() {
        PrettyTime p = new PrettyTime();
        if (currentTurn.getExpires() != null) {
            return this.name + " || Expires: " + p.format(currentTurn.getExpires().toDate());
        } else {
            return this.name + " || Last turn: " + p.format(currentTurn.getStarted().toDate());
        }

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.gameid;
        hash = 89 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Game other = (Game) obj;
        if (this.gameid != other.gameid) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Game o) {
        if (this.getCurrentTurn().started.isAfter(o.getCurrentTurn().started)) {
            return -1;
        }
        return 1;
    }

}
