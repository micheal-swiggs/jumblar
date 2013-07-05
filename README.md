<h1>Jumblar</h1>

Jumblar is an attempt to use map locations as passwords. All of our accounts require us to use passwords. Users are encouraged to have different passwords for every account. Each password should be a mix of random characters AND they should be changed regularly. Requirements that are not realistic. This is compounded by a steady stream of hacked accounts and mass state surveillance. 

Jumblar hopes to provide a pathway for increased user security. For most it is easy to remember a secret location. It could be anywhere in the world. But it is practically impossible for others to guess. By using this property of secret locations secure passwords can be generated.

<h2>Base Concepts</h2>
This section decribes how Jumblar converts & stores secret location information.  

<h4>User Secrets</h4>
Jumblar's password generation requires that a user enter a secret password and a secret location. These are combined to form what is called a 'Jumble'. 

<h4>Jumble</h4>
A Jumble is the 'part' from which passwords are generated. It is formed from a username, email, password and secret location. 

<h4>Vague Hash</h4>
The user must choose the exact same location each time that Jumblar will generate passwords. However it will be difficult for the user to zoom in and find exactly the same spot on the map. So Jumblar stores a hash of the location. Rather than storing the entire hash, Jumblar only stores a 'small' part of the hash. 

When signing in the user will attempt to choose a location as close as possible to the actual secret location. Jumblar will use the vague-hash to find the 'exact location'. The problem/benefit of the vague-hash is that it might actually lead Jumblar to an incorrect location.

If an adversary obtains the vague-hash and knows the password then the most they can achieve is knowing a set of locations that share the same vague-hash. Hence the adversary can only have a vague idea of what the location could be.

Currently the vague-hash is three bytes long, it is hoped that this will become customizable.

<h4>Open storage & PGP network</h4>
Jumblar leverages vague-hashes and the PGP network to store Jumbles in the open. Currently every Jumble is stored in the comment of a PGP entry. If you have a Jumble then everyone can see your username, email and vague-hash. 

The benefit is that your Jumble will always* be accessible. The PGP network is a decentralized group of servers. They are self-synchronizing and controlled by different organizations in different countries. 

Once a user has registered their Jumble they only require an internet connection for mapping and to contact a PGP server.
