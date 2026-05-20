Challenge 3: Crisis Intelligence & Response Orchestrator (CIRO)
Challenge Overview
Metropolitans, globally and in Pakistan, frequently faces localized crises such as:
●​ urban flooding
●​ heatwaves
●​ road blockages
●​ accidents
●​ infrastructure failures
However, response systems are:
●​ fragmented
●​ reactive
●​ slow to coordinate
Critical signals (social media, maps, weather, reports) exist — but are not converted into
actionable decisions in real time.

Problem Statement
Build an Agentic AI System that:
1.​ Ingests multi-source signals
2.​ Detects emerging crisis situations
3.​ Generates coordinated response actions
4.​ Simulates execution of those actions
5.​ Shows impact of decisions

Mandatory Requirement: Google Antigravity
Teams MUST use Google Antigravity to:
●​ orchestrate multi-agent workflows
●​ plan and execute decisions
●​ integrate tools (Maps, Search, APIs)
●​ simulate coordinated actions
Example Scenario
Input Signals:
●​ Social media:
“Flash flood happening at George Town for past 30 mins" OR

“G-10 mein pani bhar gaya hai, gaariyan phans gayi hain”

●​ Weather:
§ heavy rainfall alert
●​ Maps:
§ traffic congestion spike
Expected Output
Detected Situation:
Urban flooding (G-10/ George Town)
Confidence:
High
Impact:
- Traffic blocked
- Vehicles stranded
Recommended Actions:
- Redirect traffic via alternate routes
- Dispatch emergency services
Simulated Execution:
- Route updated on map
- Alert sent to users
- Emergency ticket created
Outcome:
Reduced congestion in simulation
System Requirements
1. Multi-Source Input Processing
· Accept:
§ text inputs (complaints, posts)
§ simulated APIs (weather, traffic)
· Handle noisy, informal language
2. Event Detection
· Identify:
§ anomalies
§ clusters
§ crisis signals
3. Reasoning & Situation Analysis
· Combine signals to:
§ infer situation
§ estimate severity
· Provide confidence level + explanation
4. Action Planning

Generate coordinated response actions:
§ routing
§ alerts
§ resource allocation
5. Action Simulation (CRITICAL)
System must simulate:
· traffic rerouting
· emergency dispatch
· alerts/notifications
Simulation examples:
· updating mock map routes
· generating emergency tickets
· sending simulated alerts
· updating system status
6. Outcome Visualization
Show:
· before vs after scenario
· impact of actions
· system logs
7. Agentic Workflow (MANDATORY)
System must demonstrate:
· multiple agents OR structured reasoning pipeline
· interaction between agents
· planning → decision → execution
·

Deliverables
1. Working Prototype with Mobile App (MUST) and Web App (Optional)
2. Demo Video (3–5 minutes)
Must show:
· multi-source input
· detected crisis
· action planning
· simulated response
· outcome
3. Agent Trace / Logs
· reasoning steps
· agent decisions
· action execution
4. Documentation (README)
Include:
· system architecture
· Antigravity usage
· tools/APIs used
· assumptions

Evaluation Criteria
1. Use of Google Antigravity — 25%
· Core orchestration handled via Antigravity
· Multi-agent planning + execution
· Tool integration
2. Agentic Reasoning & Coordination — 20%
· Multi-agent interaction
· Logical reasoning
· Decision-making quality
3. Situation Detection & Analysis — 20%
· Accuracy of event detection
· Quality of insights
· Clear explanations
4. Action Planning & Simulation — 15%
· Realistic response actions
· Effective simulation
· Clear system state change
5. Technical Implementation — 10%
· Clean architecture
· API integration
· robustness
6. Innovation & UX — 10%
· creativity
· usability
· demo clarity
Important Guidelines
●​ Use simulated datasets/APIs where needed
●​ Avoid real sensitive data
●​ Focus on decision-making, not just visualization
●​ Must show end-to-end workflow