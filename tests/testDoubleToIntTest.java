/**
 * Created by popov on 21.03.2016.
 */
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static sample.popov.PopovUtilites.PopovUtilites.booleanToStringT;
import static sample.popov.PopovUtilites.PopovUtilites.doubleToInt;

public class testDoubleToIntTest {
    @Test
    public void main() throws Exception {
        assertEquals(414, doubleToInt(414.1357657468768463576574));
    }
    @Test
    public void mainf() throws Exception {
        assertEquals("True", booleanToStringT(true));
    }
}