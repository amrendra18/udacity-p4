package com.amrendra.laughter;

import java.util.Random;

public class JokeProvider {

    private JokeProvider() {
        //no instantiation
    }

    private static String[] jokes = {
            "To err is human – and to blame it on a computer is even more so.",
            "CAPS LOCK – Preventing Login Since 1980.",
            "Programmers are tools for converting caffeine into code.",
            "My attitude isn’t bad. It’s in beta.",
            "There are three kinds of people: those who can count and those who can’t.",
            "Latest survey shows that 3 out of 4 people make up 75% of the world’s population.",
            "My software never has bugs. It just develops random features.",
            "The code that is the hardest to debug is the code that you know cannot possibly be wrong.",
            "I would love to change the world, but they won’t give me the source code."
    };

    private static int l = jokes.length;

    public static String getJoke() {
        return jokes[new Random().nextInt(l)];
    }
}
