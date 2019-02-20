// ITest.aidl
package ying.jie.aidl;

// Declare any non-default types here with import statements
import ying.jie.aidl.Person;

interface IOtherBoutique {
    void testAidl(String info);
    void setPerson(in Person person);
}
