package SpeechRecognizer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Port;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.result.WordResult;

public class SpeechRecognizerMain {
    //!-----------------Lock Variables----------------------------------------------------------
    /**
     * This variable is used to ignore the results of speech recognition cause actually it can't be stopped...
     * Check this link for more information: https://sourceforge.net/p/cmusphinx/discussion/sphinx4/thread/3875fc39/
     */
    private boolean ignoreSpeechRecognitionResults = false;
    
    // Checks if the speech recognize is already running
    private boolean speechRecognizerThreadRunning = false;

    // Checks if the resources Thread is already running
    private boolean resourcesThreadRunning;

    // This executor service is used in order the playerState events to be executed in an order
    private ExecutorService eventsExecutorService = Executors.newFixedThreadPool(2);
    
    //!-----------------End Lock Variables----------------------------------------------------------
    
    // Necessary
    private LiveSpeechRecognizer recognizer;
    
    // Logger
    private Logger logger = Logger.getLogger(getClass().getName());
    
    //.This String contains the Result that is coming back from SpeechRecognizer
    private String speechRecognitionResult;

    // variable to store object calling speech recognition 
    private SpeechCaller mover = null;
    
    // Stops ignoring the results of SpeechRecognition. Stop ignoring speech recognition results
    public synchronized void stopIgnoreSpeechRecognitionResults() { ignoreSpeechRecognitionResults = false; }
    
    // Ignores the results of SpeechRecognition. Instead of stopping the speech recognition we are ignoring it's results
    public synchronized void ignoreSpeechRecognitionResults() { ignoreSpeechRecognitionResults = true; }
    
    // Stops current speech recognizer thread
    public void stopSpeechRecognizerThread() { speechRecognizerThreadRunning = false; }

    // returns if current recognizer is ignoring speech
    public boolean getIgnoreSpeechRecognitionResults() { return ignoreSpeechRecognitionResults; }
    
    // returns if current speech recognizer thread is running
    public boolean getSpeechRecognizerThreadRunning() { return speechRecognizerThreadRunning; }

    public static void main(String[] args) {
        new SpeechRecognizerMain(null);
    }

    // Makes a decision based on the given speech words
    public void makeDecision(String speech , List<WordResult> speechWords) {
        System.out.println(speech);
        mover.Call(speech);
    }

    //!-----------------------------------------------------------------------------------------------
    // Constructor
    public SpeechRecognizerMain(SpeechCaller chessMover) {
        logger.log(Level.INFO, "Loading Speech Recognizer...\n"); // Loading Message
        Configuration configuration = new Configuration();        // Configuration
        mover = chessMover;                                       // setting mover

        // Load model from the jar
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        
        //=====================READ THIS!!!===============================================
        // Uncomment this line of code if you want the recognizer to recognize every word of the language 
        // you are using , here it is English for example	
        // *configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
        //*******************************************************************************************//
        // If you don't want to use a grammar file comment below 3 lines and uncomment the above line for language model	
        //====================================================================================
        
        // Grammar configuration
        configuration.setGrammarPath("resource:/SpeechFiles");
        configuration.setGrammarName("grammar");
        configuration.setUseGrammar(true);

        try {
            recognizer = new LiveSpeechRecognizer(configuration);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        
        // recognizer.startRecognition(true); // Start recognition process runing previously cached data.
        startResourcesThread();               // Check if needed resources are available
        startSpeechRecognition();             // Start speech recognition thread
    }
    
    //!-----------------------------------------------------------------------------------------------
    // Starts the Speech Recognition Thread
    public synchronized void startSpeechRecognition() {
        // Check lock
        if (speechRecognizerThreadRunning)
            logger.log(Level.INFO, "Speech Recognition Thread already running...\n");
        else {
            // Submit to ExecutorService
            eventsExecutorService.submit(() -> {
                speechRecognizerThreadRunning = true;                 // Set lock for thread
                ignoreSpeechRecognitionResults = true;                 // Set lock for ignore
                recognizer.startRecognition(true);                     // Start recognition
                logger.log(Level.INFO, "You can start to speak...\n"); // Log information

                try {
                    while (speechRecognizerThreadRunning) {
                        // This method will return when the end of speech is reached.
                        // Note that the end pointer will determine the end of speech.
                        SpeechResult speechResult = recognizer.getResult();

                        // Check if we ignore the speech recognition results
                        if (!ignoreSpeechRecognitionResults) {
                            // Check the result
                            if (speechResult == null)
                                logger.log(Level.INFO, "I can't understand what you said.\n");
                            else {
                                speechRecognitionResult = speechResult.getHypothesis();              // Get the hypothesis
                                System.out.println("You said: [" + speechRecognitionResult + "]\n"); // Print what you said?
                                makeDecision(speechRecognitionResult, speechResult.getWords());      // Call the appropriate method 
                                ignoreSpeechRecognitionResults = true;                               // Start ignoring
                            }
                        } else logger.log(Level.INFO, "Ignoring Speech Recognition Results...");     // print ignore results
                    }
                } catch (Exception ex) {
                    logger.log(Level.WARNING, null, ex);
                    speechRecognizerThreadRunning = false;
                }
                logger.log(Level.INFO, "SpeechThread has exited...");
            });
        }
    }

    //!-----------------------------------------------------------------------------------------------
    // Starting a Thread that checks if the resources needed to the SpeechRecognition library are available
    public void startResourcesThread() {
        // Check lock
        if (resourcesThreadRunning)
            logger.log(Level.INFO, "Resources Thread already running...\n");
        else {
            // Submit to ExecutorService
            eventsExecutorService.submit(() -> {
                try {
                    resourcesThreadRunning = true;                                    // Set Lock
                    while (true) {                                                    // Detect if the microphone is available
                        if (!AudioSystem.isLineSupported(Port.Info.MICROPHONE))       // If the Microphone is not Available
                            logger.log(Level.INFO, "Microphone is not available.\n"); //   -- log state of microphone
                        Thread.sleep(350);                                            // Sleep for some period
                    }
                }
                catch (InterruptedException ex) {
                    logger.log(Level.WARNING, null, ex);
                    resourcesThreadRunning = false;
                }
            });
        }
    }
}
