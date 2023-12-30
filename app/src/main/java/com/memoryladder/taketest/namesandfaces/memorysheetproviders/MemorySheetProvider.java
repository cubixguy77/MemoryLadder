package com.memoryladder.taketest.namesandfaces.memorysheetproviders;

import com.memoryladder.Utils;
import com.memoryladder.taketest.namesandfaces.models.NameAndFace;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MemorySheetProvider {

    public static List<NameAndFace> getTestSheet(List<String> maleFirstNames, List<String> femaleFirstNames, List<String> lastNames, int numFaces, List<Integer> maleImages, List<Integer> femaleImages) {
        List<NameAndFace> namesAndFaces = new ArrayList<>(numFaces);
        Random rand = new Random();

        int numMale;
        int numFemale;
        if (numFaces % 2 == 0) {
            numMale = numFaces / 2;
            numFemale = numFaces / 2;
        }
        else {
            numMale = numFaces / 2;
            numFemale = numFaces / 2;
            if (rand.nextInt(2) % 2 == 0)
                numMale++;
            else
                numFemale++;
        }

        List<String> shuffledMaleNames = Utils.pickNRandomElements(maleFirstNames, numMale);
        List<String> shuffledFemaleNames = Utils.pickNRandomElements(femaleFirstNames, numFemale);
        List<String> shuffledLastNames = Utils.pickNRandomElements(lastNames, numFaces);

        List<Integer> shuffledMaleImages = Utils.pickNRandomElements(maleImages, numMale);
        List<Integer> shuffledFemaleImages = Utils.pickNRandomElements(femaleImages, numFemale);

        int maleIndex = 0;
        int femaleIndex = 0;

        for (int i=0; i<numFaces; i++) {
            boolean canUseMale = maleIndex < numMale;
            boolean canUseFemale = femaleIndex < numFemale;
            boolean useMale = (canUseMale && canUseFemale && (rand.nextInt(2) == 0)) || (canUseMale && !canUseFemale);

            if (useMale) {
                NameAndFace male = new NameAndFace(shuffledMaleNames.get(maleIndex), shuffledLastNames.get(maleIndex + femaleIndex), shuffledMaleImages.get(maleIndex));
                namesAndFaces.add(male);
                maleIndex++;
            }
            else {
                NameAndFace female = new NameAndFace(shuffledFemaleNames.get(femaleIndex), shuffledLastNames.get(maleIndex + femaleIndex), shuffledFemaleImages.get(femaleIndex));
                namesAndFaces.add(female);
                femaleIndex++;
            }
        }

        return namesAndFaces;
    }
}
