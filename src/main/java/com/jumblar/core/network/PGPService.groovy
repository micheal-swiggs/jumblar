package com.jumblar.core.network

import groovy.transform.CompileStatic

@CompileStatic
public class PGPService {

    String keyserverUrl = "keyserver.ubuntu.com"
    String batchFile = "batch"

    public boolean uploadPGPRecord(String username, String comment, String email) {

        def buildStr = """
     %echo Generating a basic OpenPGP key
     Key-Type: DSA
     Key-Length: 1024
     Subkey-Type: ELG-E
     Subkey-Length: 1024
     Name-Real: ${username}
     Name-Comment: ${comment}
     Name-Email: ${email}
     Expire-Date: 0
     Passphrase: abc
     # Do a commit here, so that we can later print "done" :-)
     %commit
     %echo done
"""
        new File(batchFile).with{ f ->

            if (f.exists()) f.delete()
            f << buildStr
        }
        String commandStr = "gpg2 --batch --gen-key batch"
        def sout = new StringBuilder(), serr = new StringBuilder()
        Process p1 = commandStr.execute();
        p1.consumeProcessOutput(sout, serr)
        p1.waitFor();
        int exitValue = p1.exitValue();
        if(exitValue != 0) {

            return false
        }
        String keyId = serr.toString().split("\n").find{ it.contains("ultimately")}.split(" ")[2]


        String sendCmmd = "gpg --keyserver ${keyserverUrl} --send-keys ${keyId}"
        Process sendProc = sendCmmd.execute()
        sendProc.waitFor()
        exitValue = sendProc.exitValue()

        new File(batchFile).with{ f -> if (f.exists()) f.delete() }
        return exitValue == 0
    }
}
