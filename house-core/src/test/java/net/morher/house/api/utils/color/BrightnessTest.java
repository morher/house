package net.morher.house.api.utils.color;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.npathai.hamcrestopt.OptionalMatchers;
import org.junit.Test;

public class BrightnessTest {

  @Test
  public void testConstructorLimits() {
    assertThat(new Brightness(-2).getLevel(), is(equalTo(0.0)));
    assertThat(new Brightness(0).getLevel(), is(equalTo(0.0)));
    assertThat(new Brightness(.5).getLevel(), is(equalTo(0.5)));
    assertThat(new Brightness(1).getLevel(), is(equalTo(1.0)));
    assertThat(new Brightness(2).getLevel(), is(equalTo(1.0)));
  }

  @Test
  public void testConstructionRebase() {
    assertThat(new Brightness(10, 10, 90).getLevel(), is(equalTo(0.0)));
    assertThat(new Brightness(55, 10, 100).getLevel(), is(equalTo(0.5)));
    assertThat(new Brightness(100, 10, 100).getLevel(), is(equalTo(1.0)));

    assertThat(new Brightness(10.0, 10.0, 90.0).getLevel(), is(equalTo(0.0)));
    assertThat(new Brightness(55.0, 10.0, 100.0).getLevel(), is(equalTo(0.5)));
    assertThat(new Brightness(100.0, 10.0, 100.0).getLevel(), is(equalTo(1.0)));
  }

  @Test
  public void testOfNullable() {
    assertThat(Brightness.ofNullable(null), isEmpty());
    assertThat(Brightness.ofNullable(1.0), OptionalMatchers.isPresentAndIs(new Brightness(1.0)));

    assertThat(Brightness.ofNullable((Integer) null, 0, 100), isEmpty());
    assertThat(
        Brightness.ofNullable(50, 0, 100), OptionalMatchers.isPresentAndIs(new Brightness(0.5)));

    assertThat(Brightness.ofNullable((Double) null, 0.0, 100.0), isEmpty());
    assertThat(
        Brightness.ofNullable(50.0, 0.0, 100.0),
        OptionalMatchers.isPresentAndIs(new Brightness(0.5)));
  }

  @Test
  public void testMultiply() {
    assertThat(new Brightness(0.5).multiply(0.5).getLevel(), is(equalTo(0.25)));
  }

  @Test
  public void testRebase() {
    assertThat(new Brightness(0.5).rebase(50, 150), is(equalTo(100)));
  }
}
