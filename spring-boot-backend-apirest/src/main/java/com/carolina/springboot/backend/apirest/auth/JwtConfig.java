package com.carolina.springboot.backend.apirest.auth;

public class JwtConfig {
/*Es super importante -----BEGIN RSA PRIVATE KEY----- , sin esas anotaciones no funcionan las claves*/
		public static final String LLAVE_SECRETA="alguna.clave.secreta.12345678";
		public static final String RSA_PRIVADA="-----BEGIN RSA PRIVATE KEY-----\r\n" + 
				"MIIEpAIBAAKCAQEAvmGZSk6v4w6oS7hqoFJjuwfrr9MmLmR0njRP4huaX+rWE6YH\r\n" + 
				"RVoMb0PHVXYPyMLJBS02c6j2UucQHd+9AIHgygmDc+Gtw5TATivQzrETnbD1dHva\r\n" + 
				"h+YnrpsXFNGo+KrWJ1HDIkIal+p8jOkeAiEiWydtFgeMqUrkbBpUom6LvrCKAEni\r\n" + 
				"u+ufIZ2+ON0bk1mJWmcjtIEsAHKX2UigsCjHorBk357cERmlEsrIwkrRzI3yTEuK\r\n" + 
				"tjucZHl2tYsRf1aBxawb9BwZ7q7eJ/8ja6Jp5WJggSO6LM2tYqGLnmbyWIvsYa4/\r\n" + 
				"mNMZKk28QouKnvh+Qgv376iwyLlpG7Z717JHwwIDAQABAoIBAEPh+Q6N0Bld+JRH\r\n" + 
				"RX204uqlgFxJJawVFZ/zdyhgP0NEMOEAqkcIPLbPrBvE5QvoqFS4Mlo7PNmCmjSM\r\n" + 
				"uo+bv9NKYRzIs3SxbYByDxoj+aKYnmzSXLFoV23izvAVcfJ4t7N9a96jQ2g3rAFy\r\n" + 
				"fci0l9N402SvfHBAzAHfwZ3JXPq4yosPt3GfEyNW/p/1Wy5d9AJ1PyzAeRyTaEK7\r\n" + 
				"SSnGOdWP2x9lwtln4YRu5Y8RXe792qydOzZRv1IQry3Vxqn1D0JZhl2NkFQ0MGw9\r\n" + 
				"wA5v8AfTAv3/GYLwVGNhHXb/Nzmf8gjqv1KfqR012ZwbeV0C2Q8tO/Wx37k0GWP6\r\n" + 
				"iLCb7UECgYEA3qq+9Ki5VNOXDXOiGEEXAKwKPto4y9vaWvU0NKopYDO+fUZfejEh\r\n" + 
				"FpAQjBEgKMgmHF2+ajdlkRP1abcu2gCROitzELNG+TF1BG94Y3dJ5riyh1JuUwZ7\r\n" + 
				"gwhQtkhcruze0X/uJHqQo7A9AhTLzYxmKWCqjEGOhi9Lj6YgeXehDe0CgYEA2uGR\r\n" + 
				"kDJTR2Lcg3itFmLmOL9wa4kLDNQXLU+qEgxQhTCdD3vKW0+Yjrnv2gfEl09MH2+P\r\n" + 
				"aFKF4Nq52U0u22Mfcv3GUFMbQ5nbaQJtjLnjnaUSltO2z4IPpzNuLxRYdXRcnDsX\r\n" + 
				"kbm/CzPJ1qQCSTSeLywoPoNE9q6YrXZrXU/6dm8CgYEAnhbYUNhiDQnVGyXYFiRQ\r\n" + 
				"cpezBoDlBkfnteCV2jZd/gF+427NZvHurhhGx0t5a7r+4/DiuoLuR5zGBvKZ/wmc\r\n" + 
				"EpK6tUazw0UL8cQ601PkUj0b89vgnG3jz1peA61IsKFYaaHMsHWcr7r29b1SHXPr\r\n" + 
				"LMGHFOGOkVKO8URflh4LaNkCgYA9c9dKhNac1ETVJKxz/JBmnlkR2AgiMenP7u3w\r\n" + 
				"40Q4IM336umFeXN7nviYXlXzpW/Z45h/0auE1eQX/nOJZOsPffCP3MVq1Weks+nN\r\n" + 
				"+OLO/+mYLxjEad+7MdVdonizgVf/BRg48c2Rd5jTiniZ1QwUcnFaG8DhAWQcT4MV\r\n" + 
				"T78iQQKBgQC2lErC2rioae7zoetJe0wJRbR0YJwIIKCIUsEH2bETMN25e3XY0MAV\r\n" + 
				"8umopNpnJVLIzoj6cGVmnb4bj/0D384REGzhwJnAYnqkoXOiUmCmHGkQwdPzmW6+\r\n" + 
				"NA7jcW0zJvdeIoy21adthNT4KvEJbA5DVtQ10j/7ZFul38qq6h8mDw==\r\n" + 
				"-----END RSA PRIVATE KEY-----";
		public static final String RSA_PUBLICA="-----BEGIN PUBLIC KEY-----\r\n" + 
				"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvmGZSk6v4w6oS7hqoFJj\r\n" + 
				"uwfrr9MmLmR0njRP4huaX+rWE6YHRVoMb0PHVXYPyMLJBS02c6j2UucQHd+9AIHg\r\n" + 
				"ygmDc+Gtw5TATivQzrETnbD1dHvah+YnrpsXFNGo+KrWJ1HDIkIal+p8jOkeAiEi\r\n" + 
				"WydtFgeMqUrkbBpUom6LvrCKAEniu+ufIZ2+ON0bk1mJWmcjtIEsAHKX2UigsCjH\r\n" + 
				"orBk357cERmlEsrIwkrRzI3yTEuKtjucZHl2tYsRf1aBxawb9BwZ7q7eJ/8ja6Jp\r\n" + 
				"5WJggSO6LM2tYqGLnmbyWIvsYa4/mNMZKk28QouKnvh+Qgv376iwyLlpG7Z717JH\r\n" + 
				"wwIDAQAB\r\n" + 
				"-----END PUBLIC KEY-----";
		
}
