package com.jumblar.app.command;

import com.jumblar.app.shell.InputReader;
import com.jumblar.app.shell.ProgressCounter;
import com.jumblar.app.shell.ShellHelper;
import com.jumblar.core.controllers.BaseController;
import com.jumblar.core.controllers.PhraseController;
import com.jumblar.core.domain.*;
import com.jumblar.core.spiral.SpiralScanObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.util.StringUtils;

@ShellComponent
public class ThreePointCommands {

    @Autowired
    ShellHelper shellHelper;

    @Autowired
    InputReader inputReader;

    @Autowired
    ProgressCounter progressCounter;

    BaseController baseController = new BaseController();

    SimpleContainer simpleContainer;

    @ShellMethod("Deserialises a 3-point jumble")
    public String deserialise3point() {
        String text = acceptText("point-reference(base64)");
        PointsReference pointsReference = PointsReferenceSerializer.deserialize(text);
        return pointsReference.toString();
    }

    @ShellMethod("Makes a password given a spice and length(must be more than 3)")
    public String makePassword(@ShellOption({"-S", "--spice"}) String spice,
                               @ShellOption({"-L", "--length"}) int length,
                               @ShellOption(value={"-O", "--output"}, defaultValue = "LUD") String outputFormat){
       if (this.simpleContainer == null) {
           return shellHelper.getErrorMessage("No hashbase set, a Jumble needs to be set");
       }

       if (outputFormat.equalsIgnoreCase("hex")){
           return PhraseController.generateHexPhrase(simpleContainer.getHashBase(),
                   spice,
                   length);
       }

        return PhraseController.generatePhrase(
                simpleContainer.getHashBase(),
                spice,
                length);
    }

    @ShellMethod("Deserialises a 3-point jumble & finds actual points")
    public String load3point() {

        String text = acceptText("point-reference(base64)");
        PointsReference pointsReference = PointsReferenceSerializer.deserialize(text);
        String coord1 = acceptValidatedSecret("Coord1");
        if (coord1 == null) {
            return "";
        }
        String coord2 = acceptValidatedSecret("Coord2");
        if (coord2 == null) {
            return "";
        }
        String coord3 = acceptValidatedSecret("Coord3");
        if (coord3 == null) {
            return "";
        }

        SpiralScanObserver observer = new SpiralScanObserver();
        observer.listener = () -> {
            if (observer.getActualRounds() % 100 == 0) {
                progressCounter.display(observer.getActualRounds(), "Points scanned");
            }
        };
        long start = System.currentTimeMillis();
        HashBase hashBase = baseController
                .computeHashBaseForThreePoints(
                        pointsReference,
                        coord1,
                        coord2,
                        coord3,
                        observer);
        this.simpleContainer = new SimpleContainer(hashBase, pointsReference);
        long end = System.currentTimeMillis();
        progressCounter.display(observer.getActualRounds(), "Points scanned");
        progressCounter.reset();
        shellHelper.printInfo("Time taken: " + ((end - start) / 1000) + " seconds");

        if (hashBase != null) {
            return shellHelper.getSuccessMessage("hashbase is loaded");
        } else {
            return shellHelper.getErrorMessage("unable to find hashbase");
        }
    }

    @ShellMethod("Creates a new 3 point jumble")
    public String new3point() {

        String coord1 = acceptValidatedSecret("Coord1");
        if (coord1 == null) {
            return "";
        }
        String coord2 = acceptValidatedSecret("Coord2");
        if (coord2 == null) {
            return "";
        }
        String coord3 = acceptValidatedSecret("Coord3");
        if (coord3 == null) {
            return "";
        }

        SimpleContainer result = baseController.createNewJumbleForThreePoints(coord1, coord2, coord3, ScryptParams.BASIC);

        String serialisedPointsReference = PointsReferenceSerializer.serialise(result.getPointsReference());
        this.simpleContainer = result;
        shellHelper.printSuccess("hashbase is loaded");

        return serialisedPointsReference;
    }

    private String acceptText(String prompt) {

        String result = null;
        do {
            String password = inputReader.prompt(prompt, "", true);
            if (StringUtils.hasText(password)) {
                result = password;
            } else {
                shellHelper.printWarning("CAN NOT be empty string? Please enter valid value!");
            }
        } while (result == null);

        return result;
    }

    private String acceptValidatedSecret(String prompt) {
        String coord1Val = acceptSecret(prompt);
        String coord1Repeat = acceptSecret("repeat");

        if (!coord1Repeat.equals(coord1Val)) {
            shellHelper.printError("Both values must match.");
            return null;
        }
        return coord1Val;
    }

    private String acceptSecret(String prompt) {

        String result = null;
        do {
            String password = inputReader.prompt(prompt, "", false);
            if (StringUtils.hasText(password)) {
                result = password;
            } else {
                shellHelper.printWarning("CAN NOT be empty string? Please enter valid value!");
            }
        } while (result == null);

        return result;
    }
}
