import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import javalib.worldimages.*;
import javalib.funworld.*;
import java.awt.Color;
import tester.Tester;
import java.util.Random;

//represents a list of a given type
interface IList<T> {
  // filter this IList using the given predicate
  IList<T> filter(Predicate<T> pred);

  // map the given function onto every member of this IList
  <U> IList<U> map(Function<T, U> converter);

  // determine if any element in this Ilist satisfies the given predicate
  boolean ormap(Predicate<T> pred);

  // combine the items in this IList using the given function
  <U> U fold(BiFunction<T, U, U> converter, U initial);

  // append this list onto the given one
  IList<T> appendTo(IList<T> other);

  // add the given element onto this list
  IList<T> add(T t);

  // choose the given elements of the list, returning a new list
  IList<T> choose(IList<Integer> l);

  // choose acc
  IList<T> chooseacc(IList<Integer> l, int i);
}

//represents an empty list of the given type
class MtList<T> implements IList<T> {

  MtList() {
  }

  /*
   * FIELDS: n/a
   * 
   * METHODS: this.filter(Predicate<T>) ... IList<T> this.map(Function<T>) ...
   * IList<T> this.ormap(Predicate<T>) ... boolean this.fold(BiFunction<T U U>)
   * ... <U> this.appendTo(IList<T>) ... IList<T> this.add(IList<T>) ... IList<T>
   * this.choose(IList<Integer>) ... IList<T> this.chooseacc(IList<Integer>) ...
   * IList<T>
   * 
   * METHODS OF FIELDS: n/a
   */

  // filter this MtList using the given predicate
  public IList<T> filter(Predicate<T> pred) {
    return new MtList<T>();
  }

  // map the given function onto every member of this MtList
  public <U> IList<U> map(Function<T, U> converter) {
    return new MtList<U>();
  }

  // determine if any element in this empty list satisfies the given predicate
  public boolean ormap(Predicate<T> pred) {
    return false;
  }

  // combine the items in this MtList using the given function
  public <U> U fold(BiFunction<T, U, U> converter, U initial) {
    return initial;
  }

  // append this empty list onto the given list
  public IList<T> appendTo(IList<T> other) {
    return other;
  }

  // add the given element onto this empty list
  public IList<T> add(T t) {
    return new ConsList<T>(t, new MtList<T>());
  }

  // choose
  public IList<T> choose(IList<Integer> l) {
    return this;
  }

  // choose acc
  public IList<T> chooseacc(IList<Integer> l, int i) {
    return this;
  }
}

//represents a non empty list of the given type
class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  /*
   * FIELDS: this.first ... T this.rest ... IList<T>
   * 
   * METHODS: this.filter(Predicate<T>) ... IList<T> this.map(Function<T>) ...
   * IList<T this.ormap(Predicate<T>) ... boolean this.fold(BiFunction<T U U>) ...
   * <U> this.appendTo(IList<T>) ... IList<T> this.add(IList<T>) ... IList<T>
   * this.choose(IList<Integer>) ... IList<T> this.chooseacc(IList<Integer>) ...
   * IList<T>
   * 
   * METHODS OF FIELDS:
   * 
   */

  // filter this ConsList using the given predicate
  public IList<T> filter(Predicate<T> pred) {
    if (pred.test(this.first)) {
      return new ConsList<T>(this.first, this.rest.filter(pred));
    }
    else {
      return this.rest.filter(pred);
    }
  }

  // map the given function onto every member of this ConsList
  public <U> IList<U> map(Function<T, U> converter) {
    return new ConsList<U>(converter.apply(this.first), this.rest.map(converter));
  }

  // determine if any element in this ConsList satisfies the given predicate
  public boolean ormap(Predicate<T> pred) {
    return pred.test(this.first) || this.rest.ormap(pred);
  }

  // combine the items in this ConsList using the given function
  public <U> U fold(BiFunction<T, U, U> converter, U initial) {
    return converter.apply(this.first, this.rest.fold(converter, initial));
  }

  // append this list onto the given one
  public IList<T> appendTo(IList<T> other) {
    return this.rest.appendTo(other.add(this.first));
  }

  // add the given element onto this list
  public IList<T> add(T t) {
    return new ConsList<T>(this.first, this.rest.add(t));
  }

  // choose
  public IList<T> choose(IList<Integer> l) {
    // if the first number in the lon equals the acc, return this element of the
    // list and remove the
    // first element from the lon
    return this.chooseacc(l, 0);
  }

  // choose acc
  public IList<T> chooseacc(IList<Integer> l, int acc) {

    if (l.ormap(new Equals(acc))) {
      return new ConsList<T>(this.first, this.rest.chooseacc(l, acc + 1));
    }
    else {
      return this.rest.chooseacc(l, acc + 1);
    }

  }
}

//represents a world state with a certain amount of spaceships, invaders, invader bullets, and 
//spaceship bullets
class GamePieces extends World {
  IList<AGamePiece> spaceships;
  IList<AGamePiece> ssBullets;
  IList<AGamePiece> invaders;
  IList<AGamePiece> invBullets;

  GamePieces(IList<AGamePiece> spaceShips, IList<AGamePiece> spaceShipBullets,
      IList<AGamePiece> invs, IList<AGamePiece> invBullets) {
    this.spaceships = spaceShips;
    this.ssBullets = spaceShipBullets;
    this.invaders = invs;
    this.invBullets = invBullets;
  }

  /*
   * FIELDS: this.Spaceships ... IList<AGamePiece> this.SSBullets ...
   * IList<AGamePiece> this.Invaders ... IList<AGamePiece> this.InvBullets...
   * IList<AGamePiece>
   * 
   * METHODS: this.makeScene() ... WorldScene this.onKeyEvent(String) ... World
   * this.onTick() ... World this.lastScene(String) ... WorldScene
   * 
   * METHODS OF FIELDS: this.filter(Predicate<T>) ... IList<T>
   * this.map(Function<T>) ... IList<T this.ormap(Predicate<T>) ... boolean
   * this.fold(BiFunction<T U U>) ... <U> this.appendTo(IList<T>) ... IList<T>
   * this.add(IList<T>) ... IList<T> this.choose(IList<Integer>) ... IList<T>
   * this.chooseacc(IList<Integer>) ... IList<T>
   */

  // draws the gamepieces in each list contained in the world onto the background
  public WorldScene makeScene() {

    WorldScene spaceShip = this.spaceships.fold(new Draw(), new WorldScene(600, 400));
    WorldScene ssPlusBullets = this.ssBullets.fold(new Draw(), spaceShip);
    WorldScene ssPlusBulletsPlusInvs = this.invaders.fold(new Draw(), ssPlusBullets);
    WorldScene allDrawn = this.invBullets.fold(new Draw(), ssPlusBulletsPlusInvs);

    return allDrawn;
  }

  // shoots a Spaceship bullet if space is pressed, or changes direction
  // of spaceship if left or right arrow is pressed
  public World onKeyEvent(String key) {

    int ssBulletCount = this.ssBullets.fold(new Count(), 0);
    IList<AGamePiece> ssallowedtoFire;

    if (ssBulletCount == 3) {
      ssallowedtoFire = new MtList<AGamePiece>();
    }
    else {
      ssallowedtoFire = this.spaceships;
    }

    if (key.equals(" ")) {
      return new GamePieces(this.spaceships,
          this.ssBullets.appendTo(ssallowedtoFire.map(new Fire())), this.invaders, this.invBullets);
    }

    else {
      return new GamePieces(this.spaceships.map(new Change(key)), this.ssBullets, this.invaders,
          this.invBullets);
    }
  }

  // moves all pieces to their updated position and gets rid of pieces that have
  // been hit or gone off screen;
  // also checks if the world has ended (no spaceships or no invaders left)
  public World onTick() {

    IList<AGamePiece> newSpaceships = this.spaceships.map(new Renew());
    IList<AGamePiece> newSSBullets = this.ssBullets.map(new Renew());
    IList<AGamePiece> newInvaders = this.invaders.map(new Renew());
    IList<AGamePiece> newInvBullets = this.invBullets.map(new Renew());

    IList<AGamePiece> remainingSpaceships = newSpaceships.filter(new NotGone(newInvBullets));
    IList<AGamePiece> remainingSSBullets = newSSBullets.filter(new NotGone(newInvaders));
    IList<AGamePiece> remainingInvaders = newInvaders.filter(new NotGone(newSSBullets));
    IList<AGamePiece> remainingInvBullets = newInvBullets.filter(new NotGone(newSpaceships));

    int ssCount = remainingSpaceships.fold(new Count(), 0);
    int bulletCount = remainingInvBullets.fold(new Count(), 0);

    int invaderCount = remainingInvaders.fold(new Count(), 0);
    Utils u = new Utils();
    IList<Integer> numbers = u.buildList(4 - bulletCount, new GenerateNum(invaderCount));
    IList<AGamePiece> invsToFire = remainingInvaders.choose(numbers);
    IList<AGamePiece> allInvBullets = remainingInvBullets.appendTo(invsToFire.map(new Fire()));

    if (ssCount == 0) {

      return this.endOfWorld("You Lost!");
    }

    else if (invaderCount == 0) {
      return this.endOfWorld("You Won!");
    }

    else {
      return new GamePieces(remainingSpaceships, remainingSSBullets, remainingInvaders,
          allInvBullets);
    }
  }

  // displays the appropriate end-of-game message to the player after winning or
  // losing
  public WorldScene lastScene(String msg) {
    return new WorldScene(600, 400).placeImageXY(new TextImage(msg, 25, Color.RED), 300, 200);
  }
}

//represents a type of game piece in the game: spaceship, spaceship bullet, invader, invader bullet,
//each with an image and location
abstract class AGamePiece {
  WorldImage pic;
  CartPt loc;

  AGamePiece(WorldImage pic, CartPt loc) {
    this.pic = pic;
    this.loc = loc;
  }

  // changepiece: changes the direction of a gamepiece in response to a key press
  public abstract AGamePiece changepiece(String key);

  // renewPiece: advances the position of a gamepiece
  public abstract AGamePiece renewPiece();

  // notGone: determines if a gamepiece has been hit or gone offscreen
  public abstract boolean notGone(IList<AGamePiece> pieces);

  // overlap: determines if a gamepiece overlaps with another
  public abstract boolean overlap(AGamePiece target);

  // fire: creates a new gamepiece at this one's location
  public abstract AGamePiece fire();

  /*
   * FIELDS: this.width ... int this.height ... int this.pic ... WorldImage
   * this.loc ... CartPt
   * 
   * METHODS: this.changepiece ... AGamePiece this.renewPiece ... AGamePiece
   * this.notGone ... boolean this.overlap ... boolean this.fire ... AGamePiece
   * 
   * METHODS OF FIELDS:
   */
}

//represents a spaceship with a direction in addition to agamepiece characteristics
class Spaceship extends AGamePiece {
  String dir;

  Spaceship(WorldImage pic, CartPt loc, String dir) {
    super(pic, loc);
    this.dir = dir;
  }

  // changepiece: changes the direction of the Spaceship in response to a key
  // press
  public AGamePiece changepiece(String key) {

    if (key.equals("left")) {
      return new Spaceship(this.pic, this.loc, "left");
    }
    else if (key.equals("right")) {
      return new Spaceship(this.pic, this.loc, "right");
    }
    else {
      return this;
    }

  }

  // renew piece: advances the position of the spaceship
  public AGamePiece renewPiece() {

    if (this.loc.x <= 25 && this.dir.equals("left")) {
      return new Spaceship(this.pic, this.loc, "left");
    }

    else if (this.loc.x >= 575 && this.dir.equals("right")) {
      return new Spaceship(this.pic, this.loc, "right");
    }

    else if (this.dir.equals("left")) {
      return new Spaceship(this.pic, new CartPt(this.loc.x - 3, this.loc.y), "left");
    }

    else {
      return new Spaceship(this.pic, new CartPt(this.loc.x + 3, this.loc.y), "right");
    }

  }

  // notGone: determines if the spaceship has been hit by a bullet
  public boolean notGone(IList<AGamePiece> pieces) {
    return !pieces.ormap(new Overlap(this));
  }

  // overlap: determines if the spaceship overlaps with another
  public boolean overlap(AGamePiece target) {

    if (this.loc.x <= target.loc.x + 26.5 && this.loc.x >= target.loc.x - 26.5) {

      return this.loc.y <= target.loc.y + 13 && this.loc.y >= target.loc.y - 13;
    }

    else {
      return false;
    }

  }

  // fire: creates a new spaceship bullet at the spaceship's location
  public AGamePiece fire() {

    return new SSBullet(new RectangleImage(3, 6, OutlineMode.SOLID, Color.BLACK),
        new CartPt(this.loc.x, this.loc.y - 25));
  }

}

//represents an invader with agamepiece characteristics
class Invader extends AGamePiece {

  Invader(WorldImage pic, CartPt loc) {
    super(pic, loc);
  }

  // changepiece: returns the same invader, as invaders do not change direction
  public AGamePiece changepiece(String key) {
    return this;
  }

  // renew piece : returns the same invader, as invaders do not move
  public AGamePiece renewPiece() {
    return this;
  }

  // notGone: checks if the invader has been hit by a spaceship bullet
  public boolean notGone(IList<AGamePiece> pieces) {
    return !pieces.ormap(new Overlap(this));
  }

  // overlap: checks if the invader overlaps with another agamepiece
  public boolean overlap(AGamePiece target) {

    if (this.loc.x <= target.loc.x + 11.5 && this.loc.x >= target.loc.x - 11.5) {

      return this.loc.y >= target.loc.y - 13;
    }

    else {
      return false;
    }

  }

  // fire: fires a new Invader bullet under this invader
  public AGamePiece fire() {

    return new InvBullet(new RectangleImage(3, 6, OutlineMode.SOLID, Color.RED),
        new CartPt(this.loc.x, this.loc.y + 10));
  }

}

//represents a spaceship bullet with an image and location
class SSBullet extends AGamePiece {
  SSBullet(WorldImage pic, CartPt loc) {
    super(pic, loc);
  }

  // changepiece: returns this same bullet, as bullets do not change direction
  public AGamePiece changepiece(String key) {
    return this;
  }

  // renew piece: moves the bullet up by 5 units
  public AGamePiece renewPiece() {
    return new SSBullet(this.pic, new CartPt(this.loc.x, this.loc.y - 5));
  }

  // notGone: checks if the bullet has collided with an invader
  public boolean notGone(IList<AGamePiece> pieces) {
    return !(pieces.ormap(new Overlap(this)) || this.loc.y < 0);
  }

  // overlap: checks if the bullet overlaps with another agamepice
  public boolean overlap(AGamePiece target) {
    if (this.loc.x <= target.loc.x + 11.5 && this.loc.x >= target.loc.x - 11.5) {
      return this.loc.y <= target.loc.y + 13;
    }

    else {
      return false;
    }
  }

  // fire: returns null, as bullets cannot fire other bullets
  public AGamePiece fire() {
    return null;
  }
}

//represents an invader bullet with an image and location
class InvBullet extends AGamePiece {
  InvBullet(WorldImage pic, CartPt loc) {
    super(pic, loc);
  }

  // changepiece: returns this, as bullets do not change direction
  public AGamePiece changepiece(String key) {
    return this;
  }

  // renew piece: moves the bullet down by 3 units
  public AGamePiece renewPiece() {

    return new InvBullet(this.pic, new CartPt(this.loc.x, this.loc.y + 3));
  }

  // notGone: checks if the bullet has collided with a spaceship
  public boolean notGone(IList<AGamePiece> pieces) {
    return !(pieces.ormap(new Overlap(this)) || this.loc.y >= 400);
  }

  // overlap: checks if the bullet overlaps with another game piece
  public boolean overlap(AGamePiece target) {

    if (this.loc.x <= target.loc.x + 26.5 && this.loc.x >= target.loc.x - 26.5) {
      return this.loc.y >= target.loc.y - 13 && this.loc.y <= target.loc.y + 13;
    }

    else {
      return false;
    }

  }

  // fire: returns null, as bullets cannot fire other bullets
  public AGamePiece fire() {
    return null;
  }
}

//represents are cartesian point with an x and y location
class CartPt {
  int x;
  int y;

  CartPt(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /*
   * FIELDS: this.x ... int this.y ... int
   * 
   * METHODS: this.ptEqualto(CartPt) ... boolean
   * 
   * METHODS OF FIELDS: this.inList(CartPt) ... boolean
   * 
   */
}

//draws this agamepiece onto the given worldscene
class Draw implements BiFunction<AGamePiece, WorldScene, WorldScene> {
  public WorldScene apply(AGamePiece piece, WorldScene image) {
    return image.placeImageXY(piece.pic, piece.loc.x, piece.loc.y);
  }
}

//allows the gamepiece to respond to a key press
class Change implements Function<AGamePiece, AGamePiece> {
  String key;

  Change(String key) {
    this.key = key;
  }

  public AGamePiece apply(AGamePiece piece) {
    return piece.changepiece(this.key);
  }
}

//allows the gamepiece to renew itself with every tick
class Renew implements Function<AGamePiece, AGamePiece> {

  public AGamePiece apply(AGamePiece piece) {
    return piece.renewPiece();
  }
}

//checks if the game piece should still appear on the scene
class NotGone implements Predicate<AGamePiece> {
  IList<AGamePiece> pieces;

  NotGone(IList<AGamePiece> pieces) {
    this.pieces = pieces;
  }

  public boolean test(AGamePiece a) {
    return a.notGone(this.pieces);
  }
}

//generates a random number with a given upper bound
class GenerateNum implements Function<Integer, Integer> {
  Integer upperBound;

  // The constructor to be used when generating actual random numbers in the game
  GenerateNum(Integer upperBound) {
    this.upperBound = upperBound;
  }

  // The constructor for use in testing, with a specified Random object
  GenerateNum(Random rand, Integer upperBound) {
    this.rand = rand;
    this.upperBound = upperBound;
  }

  // Generation of the number
  Random rand = new Random();

  public Integer apply(Integer i) {
    return rand.nextInt(this.upperBound);
  }
}

//counts an element and adds it to the given current count
class Count implements BiFunction<AGamePiece, Integer, Integer> {
  public Integer apply(AGamePiece a, Integer i) {
    return i + 1;
  }
}

//allows a gamepiece to fire a bullet
class Fire implements Function<AGamePiece, AGamePiece> {

  public AGamePiece apply(AGamePiece a) {

    return a.fire();
  }
}

//checks if a game piece overlaps with another
class Overlap implements Predicate<AGamePiece> {
  AGamePiece target;

  Overlap(AGamePiece target) {
    this.target = target;
  }

  public boolean test(AGamePiece a) {

    return a.overlap(target);
  }
}

//checks if this integer equals the given one
class Equals implements Predicate<Integer> {
  Integer i;

  Equals(Integer i) {
    this.i = i;
  }

  public boolean test(Integer thisnum) {
    return thisnum == i;
  }
}

//holds useful functions not specific to one class
class Utils {

  /*
   * FIELDS: n/a
   * 
   * METHODS: this.buildList(int, Function<Integer, T>) ... ILIST
   * 
   * METHODS OF FIELDS: intToInv(Int) ... AGamePiece
   */

  // buildList: takes in an integer and function and executes that funtion n
  // number of times, while
  // passing in n-1 to the function each time, in order to construct a list of a
  // given type
  public <T> IList<T> buildList(int n, Function<Integer, T> converter) {

    if (n > 0) {
      return new ConsList<T>(converter.apply(n), buildList(n - 1, converter));
    }
    else {
      return new MtList<T>();
    }
  }
}

//creates an Invader object with an x and y location based on the given integer
class IntToInv implements Function<Integer, AGamePiece> {
  int x;
  int y;

  public AGamePiece apply(Integer n) {

    if (n > 27) {
      this.x = n * 60 - 1620;
    }
    else if (n > 18) {
      this.x = n * 60 - 1080;
    }

    else if (n > 9) {
      this.x = n * 60 - 540;
    }

    else {
      this.x = n * 60;
    }

    if (n > 27) {
      this.y = 200;
    }
    else if (n > 18) {
      this.y = 150;
    }

    else if (n > 9) {
      this.y = 100;
    }

    else {
      this.y = 50;
    }

    return new Invader(new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED), new CartPt(x, y));
  }
}

//examples and tests
class Examples {

  IList<AGamePiece> mt = new MtList<AGamePiece>();

  Utils u = new Utils();
  IList<AGamePiece> listInv = u.buildList(36, new IntToInv());
  IList<AGamePiece> listInv2 = new ConsList<AGamePiece>(
      new Invader(new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED), new CartPt(300, 100)),
      this.mt);

  WorldImage sspic = new RectangleImage(50, 20, OutlineMode.SOLID, Color.BLUE);
  WorldImage invpic = new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED);

  AGamePiece inv1 = new Invader(this.invpic, new CartPt(200, 200));
  AGamePiece ss1 = new Spaceship(this.sspic, new CartPt(300, 350), "left");
  IList<AGamePiece> sslist = new ConsList<AGamePiece>(this.ss1, this.mt);
  IList<AGamePiece> ssinvlist = new ConsList<AGamePiece>(this.inv1, this.sslist);
  IList<AGamePiece> listSSandInv = new ConsList<AGamePiece>(this.ss1, this.listInv);

  WorldImage bulletpicSS = new RectangleImage(3, 6, OutlineMode.SOLID, Color.BLACK);
  WorldImage bulletpicInv = new RectangleImage(3, 6, OutlineMode.SOLID, Color.RED);

  AGamePiece bullet1 = new SSBullet(this.bulletpicSS, new CartPt(300, 233));
  AGamePiece bullet2 = new InvBullet(this.bulletpicInv, new CartPt(120, 165));

  IList<AGamePiece> lob1 = new ConsList<AGamePiece>(this.bullet1, this.mt);
  IList<AGamePiece> lob2 = new ConsList<AGamePiece>(this.bullet2, this.lob1);

  IList<AGamePiece> SSlob = new ConsList<AGamePiece>(this.bullet1, this.mt);
  IList<AGamePiece> Invlob = new ConsList<AGamePiece>(this.bullet2, this.mt);

  IList<AGamePiece> inv0 = new ConsList<AGamePiece>(this.inv1, this.mt);
  IList<AGamePiece> invb = new ConsList<AGamePiece>(this.bullet1, this.inv0);

  IList<AGamePiece> listAll = Invlob.appendTo(listSSandInv);

  IList<AGamePiece> listofInv = u.buildList(4, new IntToInv());

  AGamePiece Bullet1of4 = new InvBullet(this.bulletpicInv, new CartPt(60, 60));
  AGamePiece Bullet2of4 = new InvBullet(this.bulletpicInv, new CartPt(120, 60));
  AGamePiece Bullet3of4 = new InvBullet(this.bulletpicInv, new CartPt(180, 60));
  AGamePiece Bullet4of4 = new InvBullet(this.bulletpicInv, new CartPt(240, 60));

  IList<AGamePiece> bullets0 = new MtList<AGamePiece>();
  IList<AGamePiece> bullets1 = new ConsList<AGamePiece>(this.Bullet1of4, this.bullets0);
  IList<AGamePiece> bullets12 = new ConsList<AGamePiece>(this.Bullet2of4, this.bullets1);
  IList<AGamePiece> bullets123 = new ConsList<AGamePiece>(this.Bullet3of4, this.bullets12);
  IList<AGamePiece> bullets1234 = new ConsList<AGamePiece>(this.Bullet4of4, this.bullets123);

  IList<Integer> lon0 = new MtList<Integer>();
  IList<Integer> lon1 = new ConsList<Integer>(1, lon0);
  IList<Integer> lon2 = new ConsList<Integer>(2, lon1);
  IList<Integer> lon3 = new ConsList<Integer>(3, lon2);
  IList<Integer> lon4 = new ConsList<Integer>(4, lon3);

  IList<Integer> nums = new ConsList<Integer>(4, new ConsList<Integer>(5,
      new ConsList<Integer>(8, new ConsList<Integer>(7, new MtList<Integer>()))));

  IList<Integer> choices = new ConsList<Integer>(1,
      new ConsList<Integer>(3, new MtList<Integer>()));

  IList<Integer> result = new ConsList<Integer>(5, new ConsList<Integer>(7, new MtList<Integer>()));

  AGamePiece Invader = new Invader(new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED),
      new CartPt(300, 100));

  AGamePiece inv35 = new Invader(this.invpic, new CartPt(480, 200));
  AGamePiece inv34 = new Invader(this.invpic, new CartPt(420, 200));
  AGamePiece inv33 = new Invader(this.invpic, new CartPt(360, 200));
  AGamePiece inv32 = new Invader(this.invpic, new CartPt(300, 200));

  IList<AGamePiece> invs1 = new ConsList<AGamePiece>(this.inv32, this.mt);
  IList<AGamePiece> invs12 = new ConsList<AGamePiece>(this.inv33, this.invs1);
  IList<AGamePiece> invs123 = new ConsList<AGamePiece>(this.inv34, this.invs12);
  IList<AGamePiece> invs1234 = new ConsList<AGamePiece>(this.inv35, this.invs123);

  // for makescene, draw and fold
  IList<AGamePiece> mtgamepieces = new MtList<AGamePiece>();
  IList<AGamePiece> spaceShip = new ConsList<AGamePiece>(this.ss1, this.mtgamepieces);
  IList<AGamePiece> ssandbullets = new ConsList<AGamePiece>(this.Bullet1of4, this.spaceShip);
  WorldScene ship = new WorldScene(600, 400).placeImageXY(sspic, 300, 350);

  // for ontick
  GamePieces world1 = new GamePieces(this.sslist, this.SSlob, this.listInv2,
      new ConsList<AGamePiece>(new InvBullet(this.bulletpicInv, new CartPt(300, 110)),
          new ConsList<AGamePiece>(new InvBullet(this.bulletpicInv, new CartPt(150, 110)),
              new ConsList<AGamePiece>(new InvBullet(this.bulletpicInv, new CartPt(200, 110)),
                  new ConsList<AGamePiece>(new InvBullet(this.bulletpicInv, new CartPt(400, 110)),
                      new MtList<AGamePiece>())))));

  GamePieces world2 = new GamePieces(
      new ConsList<AGamePiece>(new Spaceship(this.sspic, new CartPt(100, 350), "right"),
          new MtList<AGamePiece>()),

      new ConsList<AGamePiece>(new SSBullet(this.bulletpicSS, new CartPt(100, 300)),
          new MtList<AGamePiece>()),

      new ConsList<AGamePiece>(new Invader(this.invpic, new CartPt(180, 100)),
          new MtList<AGamePiece>()),

      new ConsList<AGamePiece>(new InvBullet(this.bulletpicInv, new CartPt(120, 200)),
          new ConsList<AGamePiece>(new InvBullet(this.bulletpicInv, new CartPt(180, 200)),
              new ConsList<AGamePiece>(new InvBullet(this.bulletpicInv, new CartPt(240, 110)),
                  new ConsList<AGamePiece>(new InvBullet(this.bulletpicInv, new CartPt(540, 110)),
                      new MtList<AGamePiece>())))));

  AGamePiece spaceshipright = new Spaceship(this.sspic, new CartPt(300, 350), "right");
  AGamePiece ssbullet01 = new SSBullet(this.bulletpicSS, new CartPt(300, 325));
  AGamePiece ssbullet02 = new SSBullet(this.bulletpicSS, new CartPt(300, 200));
  AGamePiece ssbullet03 = new SSBullet(this.bulletpicSS, new CartPt(300, 195));
  IList<AGamePiece> newsslist = new ConsList<AGamePiece>(this.spaceshipright, this.mt);
  IList<AGamePiece> onessbullet = new ConsList<AGamePiece>(this.ssbullet01, this.mt);
  IList<AGamePiece> twobulletlist = new ConsList<AGamePiece>(this.ssbullet02, this.onessbullet);
  IList<AGamePiece> threebulletlist = new ConsList<AGamePiece>(this.ssbullet03, this.twobulletlist);

  GamePieces gpnossbullets = new GamePieces(this.sslist, this.mt, this.listInv, this.Invlob);
  GamePieces gpfullssbullets = new GamePieces(this.sslist, this.threebulletlist, this.listInv,
      this.Invlob);

  GamePieces world3 = new GamePieces(this.sslist, this.mt, this.mt, this.mt);
  WorldScene worldscene = new WorldScene(600, 400);

  AGamePiece ss1right = new Spaceship(this.sspic, new CartPt(300, 350), "right");

  GamePieces gp = new GamePieces(this.sslist, this.SSlob, this.listInv, this.Invlob);
  GamePieces gpmt = new GamePieces(this.mt, this.mt, this.mt, this.mt);

  AGamePiece ssrightbound = new Spaceship(this.sspic, new CartPt(575, 350), "right");
  AGamePiece ssrightboundleft = new Spaceship(this.sspic, new CartPt(575, 350), "left");
  AGamePiece ssleftbound = new Spaceship(this.sspic, new CartPt(25, 350), "left");
  AGamePiece ssleftboundright = new Spaceship(this.sspic, new CartPt(25, 350), "right");

  AGamePiece invboverlappedtop = new InvBullet(this.bulletpicInv, new CartPt(300, 337));
  AGamePiece invbnotoverlapped = new InvBullet(this.bulletpicInv, new CartPt(300, 336));
  AGamePiece invboverlappedside = new InvBullet(this.bulletpicInv, new CartPt(326, 350));
  AGamePiece invbnotoverlappedside = new InvBullet(this.bulletpicInv, new CartPt(327, 350));
  AGamePiece ssboverlappedbottom = new SSBullet(this.bulletpicSS, new CartPt(200, 213));
  AGamePiece ssbnotoverlappedbottom = new SSBullet(this.bulletpicSS, new CartPt(200, 214));
  AGamePiece invbulletoverlap = new InvBullet(this.bulletpicInv, new CartPt(300, 200));
  AGamePiece ssbulletoverlap = new InvBullet(this.bulletpicInv, new CartPt(300, 200));

  // TEST FILTER
  boolean testfilter(Tester t) {
    return t.checkExpect(this.SSlob.filter(new NotGone(this.bullets1234)), this.SSlob)
        && t.checkExpect(this.SSlob.filter(new NotGone(this.lob1)), this.mt)
        && t.checkExpect(this.listInv.filter(new NotGone(this.mt)), this.listInv)
        && t.checkExpect(this.listInv2.filter(new NotGone(this.bullets1234)), this.listInv2)
        && t.checkExpect(this.bullets1234.filter(new Overlap(Bullet1of4)), this.bullets1);
  }

  // TEST CHANGE
  boolean testChange(Tester t) {
    return t.checkExpect(new Change("left").apply(this.ss1),
        new Spaceship(this.sspic, new CartPt(300, 350), "left"))
        && t.checkExpect(new Change("right").apply(this.ss1),
            new Spaceship(this.sspic, new CartPt(300, 350), "right"))
        && t.checkExpect(new Change("left").apply(this.inv1),
            new Invader(this.invpic, new CartPt(200, 200)))
        && t.checkExpect(new Change("left").apply(this.bullet1),
            new SSBullet(this.bulletpicSS, new CartPt(300, 233)))
        && t.checkExpect(new Change("right").apply(this.bullet2),
            new InvBullet(this.bulletpicInv, new CartPt(120, 165)));
  }

  // TEST RENEW
  boolean testRenew(Tester t) {
    return t.checkExpect(new Renew().apply(this.ss1),
        new Spaceship(this.sspic, new CartPt(297, 350), "left"))
        && t.checkExpect(new Renew().apply(this.inv1), this.inv1)
        && t.checkExpect(new Renew().apply(this.bullet1),
            new SSBullet(this.bulletpicSS, new CartPt(300, 228)))
        && t.checkExpect(new Renew().apply(this.bullet2),
            new InvBullet(this.bulletpicInv, new CartPt(120, 168)));
  }

  // TEST NOTGONE
  boolean testNotGone(Tester t) {
    return t.checkExpect(new NotGone(this.listInv).test(this.ss1), true) && t.checkExpect(
        new NotGone(this.listInv).test(new SSBullet(this.bulletpicSS, new CartPt(120, 50))), false)
        && t.checkExpect(
            new NotGone(this.sslist).test(new InvBullet(this.bulletpicInv, new CartPt(300, 350))),
            false)
        && t.checkExpect(new NotGone(this.sslist).test(this.inv1), true)
        && t.checkExpect(
            new NotGone(this.listInv).test(new InvBullet(this.bulletpicInv, new CartPt(450, 580))),
            false)
        && t.checkExpect(new NotGone(this.mt).test(this.ss1), true);
  }

  // TEST GENERATE NUM
  boolean testGenerateNum(Tester t) {
    return t.checkExpect(new GenerateNum(new Random(1), 5).apply(5), new Random(1).nextInt(5))
        && t.checkExpect(new GenerateNum(new Random(6), 2).apply(2), new Random(6).nextInt(2))
        && t.checkExpect(new GenerateNum(new Random(3), 1).apply(1), new Random(3).nextInt(1));
  }

  // TEST COUNT
  boolean testCount(Tester t) {
    return t.checkExpect(new Count().apply(this.inv1, 0), 1)
        && t.checkExpect(new Count().apply(this.ss1, 5), 6)
        && t.checkExpect(new Count().apply(this.bullet1, 7), 8)
        && t.checkExpect(new Count().apply(this.bullet2, 0), 1);
  }

  // TEST OVERLAP
  boolean testOverlap(Tester t) {
    return t.checkExpect(new Overlap(this.ss1).test(this.inv1), false) && t.checkExpect(
        new Overlap(this.ss1).test(new InvBullet(this.bulletpicInv, new CartPt(300, 353))), true)
        && t.checkExpect(new Overlap(this.bullet1).test(this.ss1), false)
        && t.checkExpect(
            new Overlap(this.bullet2).test(new SSBullet(this.bulletpicSS, new CartPt(120, 163))),
            true);
  }

  // TEST FIRE
  boolean testFire(Tester t) {
    return t.checkExpect(new Fire().apply(this.inv1),
        new InvBullet(this.bulletpicInv, new CartPt(200, 210)))
        && t.checkExpect(new Fire().apply(this.ss1),
            new SSBullet(this.bulletpicSS, new CartPt(300, 325)))
        && t.checkExpect(new Fire().apply(this.bullet1), null)
        && t.checkExpect(new Fire().apply(this.bullet2), null);
  }

  // TEST EQUALS
  boolean testEquals(Tester t) {
    return t.checkExpect(new Equals(0).test(5), false) && t.checkExpect(new Equals(0).test(0), true)
        && t.checkExpect(new Equals(-1).test(4), false)
        && t.checkExpect(new Equals(10).test(10), true)
        && t.checkExpect(new Equals(-11).test(11), false);
  }

  // TEST BUILDLIST
  boolean testbuildList(Tester t) {
    return t.checkExpect(new Utils().buildList(4, new IntToInv()), new ConsList<AGamePiece>(
        new Invader(new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED), new CartPt(240, 50)),
        new ConsList<AGamePiece>(
            new Invader(new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED),
                new CartPt(180, 50)),
            new ConsList<AGamePiece>(
                new Invader(new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED),
                    new CartPt(120, 50)),
                new ConsList<AGamePiece>(
                    new Invader(new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED),
                        new CartPt(60, 50)),
                    this.mt)))))

        && t.checkExpect(new Utils().buildList(0, new IntToInv()), this.mt);
  }

  // TEST INTTOINV
  boolean testIntToInv(Tester t) {
    return t.checkExpect(new IntToInv().apply(6),
        new Invader(new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED), new CartPt(360, 50)))
        && t.checkExpect(new IntToInv().apply(18),
            new Invader(new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED),
                new CartPt(540, 100)))
        && t.checkExpect(new IntToInv().apply(29), new Invader(
            new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED), new CartPt(120, 200)));
  }

  // TEST MAP

  boolean testmap(Tester t) {
    return t.checkExpect(lon4.map(new IntToInv()), this.listofInv)
        && t.checkExpect(lon2.map(new IntToInv()), u.buildList(2, new IntToInv()))
        && t.checkExpect(this.listofInv.map(new Fire()), this.bullets1234)
        && t.checkExpect(this.lon0.map(new IntToInv()), this.mt)
        && t.checkExpect(this.sslist.map(new Fire()), new ConsList<AGamePiece>(
            new SSBullet(this.bulletpicSS, new CartPt(300, 325)), this.mt));
  }

  // TEST ORMAP
  boolean testormap(Tester t) {
    return t.checkExpect(this.Invlob.ormap(new Overlap(this.ss1)), false)
        && t.checkExpect(this.SSlob.ormap(new Overlap(this.inv1)), false)
        && t.checkExpect(this.lob1.ormap(new Overlap(this.bullet1)), true)
        && t.checkExpect(this.mt.ormap(new Overlap(this.ss1)), false);
  }

  // TEST FOLD
  boolean testFold(Tester t) {

    return t.checkExpect(this.sslist.fold(new Draw(), new WorldScene(600, 400)),
        new WorldScene(600, 400).placeImageXY(this.sspic, 300, 350))
        && t.checkExpect(this.mt.fold(new Draw(), new WorldScene(600, 400)),
            new WorldScene(600, 400))
        && t.checkExpect(this.ssinvlist.fold(new Draw(), new WorldScene(600, 400)),
            new WorldScene(600, 400).placeImageXY(this.invpic, 200, 200).placeImageXY(this.sspic,
                300, 350))
        && t.checkExpect(this.Invlob.fold(new Count(), 0), 1)
        && t.checkExpect(this.listInv.fold(new Count(), 0), 36);
  }

  // TEST APPENDTO
  boolean testappendTo(Tester t) {
    return t.checkExpect(this.SSlob.appendTo(Invlob),
        new ConsList<AGamePiece>(this.bullet2, new ConsList<AGamePiece>(this.bullet1, this.mt)))
        && t.checkExpect(this.mt.appendTo(listAll), this.listAll) && t
            .checkExpect(this.Invlob.appendTo(SSlob),
                new ConsList<AGamePiece>(this.bullet1,
                    new ConsList<AGamePiece>(this.bullet2, this.mt)))
        && t.checkExpect(this.sslist.appendTo(this.listInv2),
            new ConsList<AGamePiece>(
                new Invader(new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED),
                    new CartPt(300, 100)),
                new ConsList<AGamePiece>(this.ss1, this.mt)));

  }

  // TEST ADD
  boolean testAdd(Tester t) {
    return t.checkExpect(this.lon0.add(5), new ConsList<Integer>(5, this.lon0))
        && t.checkExpect(this.lon1.add(6),
            new ConsList<Integer>(1, new ConsList<Integer>(6, this.lon0)))
        && t.checkExpect(this.lon3.add(-1),
            new ConsList<Integer>(3,
                new ConsList<Integer>(2,
                    new ConsList<Integer>(1, new ConsList<Integer>(-1, this.lon0)))))
        && t.checkExpect(this.lob1.add(this.inv1),
            new ConsList<AGamePiece>(this.bullet1, new ConsList<AGamePiece>(this.inv1, this.mt)));
  }

  // TEST CHOOSE
  boolean testchoose(Tester t) {
    return t.checkExpect(nums.choose(this.choices), this.result)
        && t.checkExpect(this.listInv.choose(this.lon4), this.invs1234)
        && t.checkExpect(this.listInv.choose(lon0), this.mt)
        && t.checkExpect(this.sslist.choose(new ConsList<Integer>(0, this.lon0)), this.sslist);
  }

  // TEST CHOOSE ACC
  boolean testchooseacc(Tester t) {
    return t.checkExpect(nums.chooseacc(this.choices, 0), this.result)
        && t.checkExpect(this.listInv.chooseacc(this.lon4, 0), this.invs1234)
        && t.checkExpect(this.listInv.chooseacc(lon0, 0), this.mt) && t.checkExpect(
            this.sslist.chooseacc(new ConsList<Integer>(0, this.lon0), 0), this.sslist);
  }

  // TEST ON TICK
  boolean testonTick(Tester t) {
    return t.checkExpect(world1.onTick(),

        new GamePieces(

            new ConsList<AGamePiece>(new Spaceship(this.sspic, new CartPt(297, 350), "left"),
                new MtList<AGamePiece>()),

            new ConsList<AGamePiece>(new SSBullet(this.bulletpicSS, new CartPt(300, 228)),
                new MtList<AGamePiece>()),

            this.listInv2,

            new ConsList<AGamePiece>(new InvBullet(this.bulletpicInv, new CartPt(300, 113)),
                new ConsList<AGamePiece>(new InvBullet(this.bulletpicInv, new CartPt(150, 113)),
                    new ConsList<AGamePiece>(new InvBullet(this.bulletpicInv, new CartPt(200, 113)),
                        new ConsList<AGamePiece>(
                            new InvBullet(this.bulletpicInv, new CartPt(400, 113)),
                            new MtList<AGamePiece>()))))))

        && t.checkExpect(this.world2.onTick(),

            new GamePieces(

                new ConsList<AGamePiece>(new Spaceship(this.sspic, new CartPt(103, 350), "right"),
                    new MtList<AGamePiece>()),

                new ConsList<AGamePiece>(new SSBullet(this.bulletpicSS, new CartPt(100, 295)),
                    new MtList<AGamePiece>()),

                new ConsList<AGamePiece>(new Invader(this.invpic, new CartPt(180, 100)),
                    new MtList<AGamePiece>()),

                new ConsList<AGamePiece>(new InvBullet(this.bulletpicInv, new CartPt(120, 203)),
                    new ConsList<AGamePiece>(new InvBullet(this.bulletpicInv, new CartPt(180, 203)),
                        new ConsList<AGamePiece>(
                            new InvBullet(this.bulletpicInv, new CartPt(240, 113)),
                            new ConsList<AGamePiece>(
                                new InvBullet(this.bulletpicInv, new CartPt(540, 113)),
                                new MtList<AGamePiece>()))))));
  }

  // TEST ON KEY EVENT

  boolean testonkeyEvent(Tester t) {

    // testing moving the spaceship with arrows in the world

    // spaceship already in the left direction
    return t.checkExpect(this.gp.onKeyEvent("left"), this.gp)

        // changing direction of spaceship to the right
        && t.checkExpect(this.gp.onKeyEvent("right"),
            new GamePieces(this.newsslist, this.SSlob, this.listInv, this.Invlob))

        // testing firing spaceship bullets with one on the screen
        && t.checkExpect(this.gpnossbullets.onKeyEvent(" "),
            new GamePieces(this.sslist, this.onessbullet, this.listInv, this.Invlob))

        // testing the attempt to fire a spaceship bullet when three are already in
        // flight -> no more can be fired
        && t.checkExpect(this.gpfullssbullets.onKeyEvent(" "), this.gpfullssbullets);
  }

  // TEST BIG BANG
  boolean testBigBang(Tester t) {
    GamePieces world = new GamePieces(this.sslist, this.mt, this.listInv, this.mt);
    int worldWidth = 600;
    int worldHeight = 400;
    double tickRate = .02;
    return world.bigBang(worldWidth, worldHeight, tickRate);
  }

  // TEST MAKESCENE, DRAW, FOLD

  boolean testmakeScene(Tester t) {

    // test drawing the empty canvas
    return t.checkExpect(this.mtgamepieces.fold(new Draw(), new WorldScene(600, 400)),
        new WorldScene(600, 400))

        // tests drawing the spaceship on the canvas
        && t.checkExpect(this.spaceShip.fold(new Draw(), new WorldScene(600, 400)),
            new WorldScene(600, 400).placeImageXY(sspic, 300, 350))

        // tests drawing a bullet onto an already built WorldScene
        && t.checkExpect(this.ssandbullets.fold(new Draw(), this.ship),
            this.ship.placeImageXY(this.bulletpicInv, 60, 60))
        && t.checkExpect(new Draw().apply(this.ss1, this.worldscene),
            this.worldscene.placeImageXY(this.sspic, 300, 350))

        // call makeScene on a world
        && t.checkExpect(world3.makeScene(),
            new WorldScene(600, 400).placeImageXY(sspic, 300, 350));
  }

  // TEST ON KEY

  boolean testchangePiece(Tester t) {

    // invalid key press doesn't change the Spaceship AGamePiece
    return t.checkExpect(this.ss1.changepiece("h"), this.ss1)

        // using left key press of a Spaceship AGamePiece going left, doesn't change
        // it's course
        && t.checkExpect(this.ss1.changepiece("left"), this.ss1)

        // using a right key press of a Spaceship AGamePiece going left, changes
        // direction to right
        && t.checkExpect(this.ss1.changepiece("right"), this.ss1right)

        // using a different AGamePiece other than a SpaceShip AGamePiece
        && t.checkExpect(this.inv1.changepiece("right"), this.inv1)

        // using a SpaceShip Bullet AGamePiece
        && t.checkExpect(this.bullet1.changepiece("left"), this.bullet1)

        // using an Invader Bullet AGamePiece
        && t.checkExpect(this.bullet2.changepiece("right"), this.bullet2);
  }

  // TEST LAST SCENE

  boolean testLastScene(Tester t) {

    // both tests done on an initial world scene
    return t.checkExpect(this.gp.lastScene("You Win"),
        new WorldScene(600, 400).placeImageXY(new TextImage("You Win", 25, Color.RED), 300, 200))
        && t.checkExpect(this.gp.lastScene("You Lose"), new WorldScene(600, 400)
            .placeImageXY(new TextImage("You Lose", 25, Color.RED), 300, 200))

        // method works for an scene (including empty)
        && t.checkExpect(this.gp.lastScene("You Win"), new WorldScene(600, 400)
            .placeImageXY(new TextImage("You Win", 25, Color.RED), 300, 200))

        // empty case
        && t.checkExpect(this.gpmt.lastScene("You Lose"), new WorldScene(600, 400)
            .placeImageXY(new TextImage("You Lose", 25, Color.RED), 300, 200));

  }

  // TEST RENEW PIECE

  boolean testrenewPiece(Tester t) {

    // testing renewpiece by moving a spaceship
    // starting center spaceship and moving left -> tests to make sure new Spaceship
    // implemented

    return t.checkExpect(this.ss1.renewPiece(),
        new Spaceship(this.sspic, new CartPt(297, 350), "left"))

        // starting center spaceship and moving right -> tests to make sure new
        // Spaceship implemented
        && t.checkExpect(this.ss1right.renewPiece(),
            new Spaceship(this.sspic, new CartPt(303, 350), "right"))

        // testing when spaceship is at the rightmost boundary going right -> test to
        // make sure same Spaceship implemented
        && t.checkExpect(this.ssrightbound.renewPiece(), this.ssrightbound)

        // testing when spaceship at rightmost boundary going left -> spaceship renewed
        // to the left
        && t.checkExpect(this.ssrightboundleft.renewPiece(),
            new Spaceship(this.sspic, new CartPt(572, 350), "left"))

        // testing when spaceship at leftmost boundary going left -> same Spaceship
        // implemented
        && t.checkExpect(this.ssleftbound.renewPiece(), this.ssleftbound)

        // testing when spaceship at leftmost boundary going right -> spaceship renewed
        // to the right
        && t.checkExpect(this.ssleftboundright.renewPiece(),
            new Spaceship(this.sspic, new CartPt(28, 350), "right"))

        // testing renewpiece on an invader AGamePiece -> Will not renew
        && t.checkExpect(this.inv1.renewPiece(), this.inv1)

        // testing renewpiece on a spaceshipbullet AGamePiece -> Will move down across
        // the screen
        && t.checkExpect(this.bullet1.renewPiece(),
            new SSBullet(this.bulletpicSS, new CartPt(300, 228)))

        // testing renewpiece on a invaderbullet AGamePiece -> Will move up the screen
        && t.checkExpect(this.bullet2.renewPiece(),
            new InvBullet(this.bulletpicInv, new CartPt(120, 168)));
  }

  // TEST overlap

  boolean testoverlap(Tester t) {

    // testing overlap for a Spaceship and Invader Bullet
    // testing invaderbullet that hits front of the spaceship
    return t.checkExpect(this.ss1.overlap(this.invboverlappedtop), true)

        // invader bullet one byte away -> no overlap
        && t.checkExpect(this.ss1.overlap(this.invbnotoverlapped), false)

        // invader bullet hitting right side of the spaceship
        && t.checkExpect(this.ss1.overlap(this.invboverlappedside), true)

        // invader bullet one byte away from hitting the spaceship
        && t.checkExpect(this.ss1.overlap(this.invbnotoverlappedside), false)

        // same tests, but invbullet calling overlap method and spaceship passed as
        // argument
        && t.checkExpect(this.invboverlappedtop.overlap(this.ss1), true)
        && t.checkExpect(this.invbnotoverlapped.overlap(this.ss1), false)
        && t.checkExpect(this.invboverlappedside.overlap(this.ss1), true)
        && t.checkExpect(this.invbnotoverlappedside.overlap(this.ss1), false)

        // testing overlap between spaceship and invader -> will never overlap
        && t.checkExpect(this.inv1.overlap(this.ss1), false)
        && t.checkExpect(this.ss1.overlap(this.inv1), false)

        // testing overlap (hit) between invader and spaceship bullet
        && t.checkExpect(this.inv1.overlap(this.ssboverlappedbottom), true)
        && t.checkExpect(this.inv1.overlap(this.ssbnotoverlappedbottom), false)

        // not testing contact on the side of the invader or ssbullet because bullets
        // move
        // vertically and invaders remaind stationary
        // testing flipped caller and argument of method
        && t.checkExpect(this.ssboverlappedbottom.overlap(this.inv1), true)
        && t.checkExpect(this.ssbnotoverlappedbottom.overlap(this.inv1), false)

        // testing overlapping spaceship and invader bullets
        // important note!! : While overlap method true, these overlapped instances of
        // bullets
        // aren't removed from the notGone method -> they simply pass over each other
        && t.checkExpect(this.invbulletoverlap.overlap(this.ssbulletoverlap), true)
        && t.checkExpect(this.ssbulletoverlap.overlap(this.invbulletoverlap), true);
  }

  // TEST fire()
  // note: fire() method only operates within Spaceship and Invader AGamePieces
  // bullets return null values as they can't fire

  boolean testfire(Tester t) {
    // testing fire method from a Spaceship AGamePiece -> creates a ssbullet from
    // the spaceship's current location
    // testing from the original location

    return t.checkExpect(this.ss1.fire(), new SSBullet(this.bulletpicSS, new CartPt(300, 325)))

        // testing from a new SpaceShip location
        && t.checkExpect(this.ssrightbound.fire(),
            new SSBullet(this.bulletpicSS, new CartPt(575, 325)))

        // testing fire method from an InvaderAGamePiece -> creates an invbullet from
        // the chosen invader location
        && t.checkExpect(this.inv1.fire(), new InvBullet(this.bulletpicInv, new CartPt(200, 210)))

        // testing from a new invader
        && t.checkExpect(this.inv32.fire(), new InvBullet(this.bulletpicInv, new CartPt(300, 210)))

        // testing fire method on spaceship bullets and invader bullets -> return null
        && t.checkExpect(this.invbnotoverlapped.fire(), null)
        && t.checkExpect(this.ssbulletoverlap.fire(), null);
  }
}
