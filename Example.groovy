@GrabResolver(name='jitpack', root='https://jitpack.io/')
@Grab('com.github.micheal-swiggs:jumblar:v0.2.0')
@GrabExclude('org.codehaus.groovy:groovy-all')

/**
 * Run with :
 *  groovy Example.groovy
 */
import com.jumblar.core.domain.SimpleJumble;
import com.jumblar.core.controllers.BaseController;

println "Welcome to the Jumblar"



String username = "your-username";
String email = "your-addy@special.domain";
String personalInfo = "";
String password = "password";
String coordinate = "48.858404,2.293571"; //Corner of eiffel tower.
int N = 1024;
int r = 8;
int p = 1;
int keyLength = 64;
BaseController bc = new BaseController();

/**
 * Upload new user info to PGP servers.
 */
SimpleJumble originalJumble = bc.createNewPGPEntry(username, email, personalInfo, password, coordinate,
        N, r, p, keyLength);

byte[] original = originalJumble.getHashBase().getHashBase()

/**
 * Simulates a user signing in.
 */
String guessCoordinate = "48.858405,2.293577";
SimpleJumble guessJumble = bc.computeHashBase(username, email, personalInfo, password, guessCoordinate);
byte[] guess = guessJumble.getHashBase().getHashBase()

println Arrays.equals(original, guess)

