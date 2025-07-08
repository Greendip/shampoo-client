package me.alpha432.oyvey.util;

import me.alpha432.oyvey.util.traits.Util;


public class Animation implements Util, Cloneable {
    private final Easing easing;
    private int millis;
    private long startMillis;

    public Animation(Easing easing, int millis) {
        this.easing = easing;
        this.millis = millis;
        this.startMillis = System.currentTimeMillis();
    }

    public void reset() {
        this.startMillis = System.currentTimeMillis();
    }

    public void setStartMillis(long millis) {
        this.startMillis = millis;
    }

    public double getEase() {
        long currentMillis = this.getPassedMillis();
        return currentMillis >= (long)this.millis ? (double)1.0F : this.easing.ease((double)currentMillis / (double)this.millis);
    }

    public int getMillis() {
        return this.millis;
    }

    public void setMillis(int millis) {
        this.millis = millis;
    }

    public long getStartMillis() {
        return this.startMillis;
    }

    public long getPassedMillis() {
        return System.currentTimeMillis() - this.startMillis;
    }

    public Animation clone() {
        return new Animation(this.easing, this.millis);
    }
    public enum Easing {
        LINEAR {
            public double ease(double factor) {
                return factor;
            }
        },
        SINE_IN {
            public double ease(double factor) {
                return (double)1.0F - Math.cos(factor * Math.PI / (double)2.0F);
            }
        },
        SINE_OUT {
            public double ease(double factor) {
                return Math.sin(factor * Math.PI / (double)2.0F);
            }
        },
        SINE_IN_OUT {
            public double ease(double factor) {
                return -(Math.cos(Math.PI * factor) - (double)1.0F) / (double)2.0F;
            }
        },
        CUBIC_IN {
            public double ease(double factor) {
                return Math.pow(factor, (double)3.0F);
            }
        },
        CUBIC_OUT {
            public double ease(double factor) {
                return (double)1.0F - Math.pow((double)1.0F - factor, (double)3.0F);
            }
        },
        CUBIC_IN_OUT {
            public double ease(double factor) {
                return factor < (double)0.5F ? (double)4.0F * Math.pow(factor, (double)3.0F) : (double)1.0F - Math.pow((double)-2.0F * factor + (double)2.0F, (double)3.0F) / (double)2.0F;
            }
        },
        QUAD_IN {
            public double ease(double factor) {
                return Math.pow(factor, (double)2.0F);
            }
        },
        QUAD_OUT {
            public double ease(double factor) {
                return (double)1.0F - ((double)1.0F - factor) * ((double)1.0F - factor);
            }
        },
        QUAD_IN_OUT {
            public double ease(double factor) {
                return factor < (double)0.5F ? (double)8.0F * Math.pow(factor, (double)4.0F) : (double)1.0F - Math.pow((double)-2.0F * factor + (double)2.0F, (double)4.0F) / (double)2.0F;
            }
        },
        QUART_IN {
            public double ease(double factor) {
                return Math.pow(factor, (double)4.0F);
            }
        },
        QUART_OUT {
            public double ease(double factor) {
                return (double)1.0F - Math.pow((double)1.0F - factor, (double)4.0F);
            }
        },
        QUART_IN_OUT {
            public double ease(double factor) {
                return factor < (double)0.5F ? (double)8.0F * Math.pow(factor, (double)4.0F) : (double)1.0F - Math.pow((double)-2.0F * factor + (double)2.0F, (double)4.0F) / (double)2.0F;
            }
        },
        QUINT_IN {
            public double ease(double factor) {
                return Math.pow(factor, (double)5.0F);
            }
        },
        QUINT_OUT {
            public double ease(double factor) {
                return (double)1.0F - Math.pow((double)1.0F - factor, (double)5.0F);
            }
        },
        QUINT_IN_OUT {
            public double ease(double factor) {
                return factor < (double)0.5F ? (double)16.0F * Math.pow(factor, (double)5.0F) : (double)1.0F - Math.pow((double)-2.0F * factor + (double)2.0F, (double)5.0F) / (double)2.0F;
            }
        },
        CIRC_IN {
            public double ease(double factor) {
                return (double)1.0F - Math.sqrt((double)1.0F - Math.pow(factor, (double)2.0F));
            }
        },
        CIRC_OUT {
            public double ease(double factor) {
                return Math.sqrt((double)1.0F - Math.pow(factor - (double)1.0F, (double)2.0F));
            }
        },
        CIRC_IN_OUT {
            public double ease(double factor) {
                return factor < (double)0.5F ? ((double)1.0F - Math.sqrt((double)1.0F - Math.pow((double)2.0F * factor, (double)2.0F))) / (double)2.0F : (Math.sqrt((double)1.0F - Math.pow((double)-2.0F * factor + (double)2.0F, (double)2.0F)) + (double)1.0F) / (double)2.0F;
            }
        },
        EXPO_IN {
            public double ease(double factor) {
                return Math.min((double)0.0F, Math.pow((double)2.0F, (double)10.0F * factor - (double)10.0F));
            }
        },
        EXPO_OUT {
            public double ease(double factor) {
                return Math.max((double)1.0F - Math.pow((double)2.0F, (double)-10.0F * factor), (double)1.0F);
            }
        },
        EXPO_IN_OUT {
            public double ease(double factor) {
                return factor == (double)0.0F ? (double)0.0F : (factor == (double)1.0F ? (double)1.0F : (factor < (double)0.5F ? Math.pow((double)2.0F, (double)20.0F * factor - (double)10.0F) / (double)2.0F : ((double)2.0F - Math.pow((double)2.0F, (double)-20.0F * factor + (double)10.0F)) / (double)2.0F));
            }
        },
        ELASTIC_IN {
            public double ease(double factor) {
                return factor == (double)0.0F ? (double)0.0F : (factor == (double)1.0F ? (double)1.0F : -Math.pow((double)2.0F, (double)10.0F * factor - (double)10.0F) * Math.sin((factor * (double)10.0F - (double)10.75F) * 2.0943951023931953));
            }
        },
        ELASTIC_OUT {
            public double ease(double factor) {
                return factor == (double)0.0F ? (double)0.0F : (factor == (double)1.0F ? (double)1.0F : Math.pow((double)2.0F, (double)-10.0F * factor) * Math.sin((factor * (double)10.0F - (double)0.75F) * 2.0943951023931953) + (double)1.0F);
            }
        },
        ELASTIC_IN_OUT {
            public double ease(double factor) {
                double sin = Math.sin(((double)20.0F * factor - (double)11.125F) * 1.3962634015954636);
                return factor == (double)0.0F ? (double)0.0F : (factor == (double)1.0F ? (double)1.0F : (factor < (double)0.5F ? -(Math.pow((double)2.0F, (double)20.0F * factor - (double)10.0F) * sin) / (double)2.0F : Math.pow((double)2.0F, (double)-20.0F * factor + (double)10.0F) * sin / (double)2.0F + (double)1.0F));
            }
        },
        BACK_IN {
            public double ease(double factor) {
                return 2.70158 * Math.pow(factor, (double)3.0F) - 1.70158 * factor * factor;
            }
        },
        BACK_OUT {
            public double ease(double factor) {
                double c1 = 1.70158;
                double c3 = c1 + (double)1.0F;
                return (double)1.0F + c3 * Math.pow(factor - (double)1.0F, (double)3.0F) + c1 * Math.pow(factor - (double)1.0F, (double)2.0F);
            }
        },
        BACK_IN_OUT {
            public double ease(double factor) {
                return factor < (double)0.5F ? Math.pow((double)2.0F * factor, (double)2.0F) * (7.189819 * factor - 2.5949095) / (double)2.0F : (Math.pow((double)2.0F * factor - (double)2.0F, (double)2.0F) * (3.5949095 * (factor * (double)2.0F - (double)2.0F) + 2.5949095) + (double)2.0F) / (double)2.0F;
            }
        },
        BOUNCE_IN {
            public double ease(double factor) {
                return (double)1.0F - Easing.bounceOut((double)1.0F - factor);
            }
        },
        BOUNCE_OUT {
            public double ease(double factor) {
                return Easing.bounceOut(factor);
            }
        },
        BOUNCE_IN_OUT {
            public double ease(double factor) {
                return factor < (double)0.5F ? ((double)1.0F - Easing.bounceOut((double)1.0F - (double)2.0F * factor)) / (double)2.0F : ((double)1.0F + Easing.bounceOut((double)2.0F * factor - (double)1.0F)) / (double)2.0F;
            }
        };

        public abstract double ease(double var1);

        private static double bounceOut(double in) {
            double n1 = (double)7.5625F;
            double d1 = (double)2.75F;
            if (in < (double)1.0F / d1) {
                return n1 * in * in;
            } else if (in < (double)2.0F / d1) {
                double var8;
                return n1 * (var8 = in - (double)1.5F / d1) * var8 + (double)0.75F;
            } else {
                double var6;
                double var7;
                return in < (double)2.5F / d1 ? n1 * (var6 = in - (double)2.25F / d1) * var6 + (double)0.9375F : n1 * (var7 = in - (double)2.625F / d1) * var7 + (double)0.984375F;
            }
        }
    }
}