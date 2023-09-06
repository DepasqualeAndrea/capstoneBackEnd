package BackEnd.CapstoneProject.Security;
@Component
public class JWTFilter {
	@Autowired
	JWTTools jTools;
	@Autowired
	UtenteService utenteService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header = request.getHeader("Authorization");
		if (header == null || !header.startsWith("Bearer "))
			throw new UnauthorizedException("Inserisci il token nell'autorization Header");
		String token = header.substring(7);
		System.out.println("Token: -> " + token);

		jTools.verificaToken(token);
		String id = jTools.extractSubject(token);
		Utente utenteCorrente = utenteService.findById(UUID.fromString(id));

		UsernamePasswordAuthenticationToken autorizzationToken = new UsernamePasswordAuthenticationToken(utenteCorrente,
				null, utenteCorrente.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(autorizzationToken);

		filterChain.doFilter(request, response);

	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		System.out.println(request.getServletPath());
		return new AntPathMatcher().match("/auth/**", request.getServletPath());
	}
}
